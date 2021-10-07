package com.example;

import java.util.concurrent.Callable;

public class DoubleCheckLockingCache<V> implements Callable<V> {

	private final Callable<V> callable;
	private volatile V value;

	public DoubleCheckLockingCache(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public V call() throws Exception {
		if (value == null) {
			synchronized (this) {
				if (value == null) {
					value = callable.call();
				}
			}
		}
		return value;
	}
}
