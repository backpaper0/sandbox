package com.example;

import java.util.concurrent.Callable;

public class SynchronizedCache<V> implements Callable<V> {

	private final Callable<V> callable;
	private V value;

	public SynchronizedCache(Callable<V> callable) {
		this.callable = callable;
	}

	@Override
	public synchronized V call() throws Exception {
		if (value == null) {
			value = callable.call();
		}
		return value;
	}
}
