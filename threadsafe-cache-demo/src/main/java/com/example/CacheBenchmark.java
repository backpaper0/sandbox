package com.example;

import java.util.concurrent.Callable;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public class CacheBenchmark {

	private final Callable<String> callable = () -> "HelloWorld";
	private final AtomicBooleanCache<String> atomicBooleanCache = new AtomicBooleanCache<>(callable);
	private final DoubleCheckLockingCache<String> doubleCheckLockingCache = new DoubleCheckLockingCache<>(callable);
	private final FutureTaskCache<String> futureTaskCache = new FutureTaskCache<>(callable);
	private final SynchronizedCache<String> synchronizedCache = new SynchronizedCache<>(callable);

	@Benchmark
	public String atomicBooleanCache() throws Exception {
		return atomicBooleanCache.call();
	}

	@Benchmark
	public String doubleCheckLockingCache() throws Exception {
		return doubleCheckLockingCache.call();
	}

	@Benchmark
	public String futureTaskCache() throws Exception {
		return futureTaskCache.call();
	}

	@Benchmark
	public String synchronizedCache() throws Exception {
		return synchronizedCache.call();
	}
}
