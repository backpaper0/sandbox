package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class Caller implements AfterEachCallback {

	private ExecutorService executor;

	private final int size;

	public Caller() {
		this(10);
	}

	public Caller(int size) {
		this.size = size;
	}

	public <V> void callAndAssert(Callable<V> callable) throws Exception {
		executor = Executors.newFixedThreadPool(size);
		CyclicBarrier barrier = new CyclicBarrier(size);
		List<Future<V>> futures = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			Future<V> future = executor.submit(() -> {
				barrier.await();
				return callable.call();
			});
			futures.add(future);
		}
		List<V> results = new ArrayList<>(size);
		for (Future<V> future : futures) {
			V value = future.get(10, TimeUnit.SECONDS);
			assertNotNull(value);
			results.add(value);
		}
		assertEquals(1L, results.stream().distinct().count());
	}

	@Override
	public void afterEach(ExtensionContext context) throws Exception {
		if (executor != null) {
			executor.shutdown();
		}
	}
}
