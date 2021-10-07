package com.example;

import java.util.UUID;
import java.util.concurrent.Callable;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

public class CacheTest {

	@RegisterExtension
	Caller caller = new Caller();

	private final Callable<UUID> callable = UUID::randomUUID;

	@Test
	void testAtomicBooleanCache() throws Exception {
		caller.callAndAssert(new AtomicBooleanCache<>(callable));
	}

	@Test
	void testDoubleCheckLockingCache() throws Exception {
		caller.callAndAssert(new SynchronizedCache<>(callable));
	}

	@Test
	void testSynchronizedCache() throws Exception {
		caller.callAndAssert(new SynchronizedCache<>(callable));
	}

	@Test
	void testFutureTaskCache() throws Exception {
		caller.callAndAssert(new FutureTaskCache<>(callable));
	}
}
