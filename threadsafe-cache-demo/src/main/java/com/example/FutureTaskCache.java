package com.example;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

public class FutureTaskCache<V> implements Callable<V> {

	private final Callable<V> callable;
	private final AtomicReference<FutureTask<V>> ref = new AtomicReference<>();

	public FutureTaskCache(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public V call() throws Exception {
		FutureTask<V> task = ref.get();
		if (task == null) {
			task = new FutureTask<>(callable);
			if (ref.compareAndSet(null, task)) {
				task.run();
			} else {
				task = ref.get();
			}
		}
		return task.get();
	}
}
