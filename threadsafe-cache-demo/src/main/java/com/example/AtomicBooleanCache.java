package com.example;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanCache<V> implements Callable<V> {

	private final Callable<V> callable;
	private final AtomicBoolean initialized = new AtomicBoolean(false);
	private volatile V value;

	public AtomicBooleanCache(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public V call() throws Exception {
		if (value == null && !initialized.get() && initialized.compareAndSet(false, true)) {
			value = callable.call();
		}
		// Spinlock
		while (value == null) {
		}
		return value;
	}
}
