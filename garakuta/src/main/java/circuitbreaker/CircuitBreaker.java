package circuitbreaker;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/**
 * CircuitBreaker
 *
 * @see <a href="https://martinfowler.com/bliki/CircuitBreaker.html">https://martinfowler.com/bliki/CircuitBreaker.html</a>
 *
 */
public class CircuitBreaker {

	private final int failureThreshold;
	private final long resetTimeout;
	private final LongSupplier clock;

	private int failureCount = 0;
	private long lastFailureTime = 0;

	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public CircuitBreaker(int failureThreshold, long resetTimeout, LongSupplier clock) {
		if (failureThreshold < 1) {
			throw new IllegalArgumentException("failureThreshold must be greather than 0");
		}
		if (resetTimeout < 1) {
			throw new IllegalArgumentException("resetTimeout must be greather than 0");
		}
		this.failureThreshold = failureThreshold;
		this.resetTimeout = resetTimeout;
		this.clock = Objects.requireNonNull(clock, "clock is required");
	}

	public <T> T execute(Callable<? extends T> callable) {
		final State state = state();
		switch (state) {
		case CLOSED:
		case HALF_OPEN:
			try {
				final T result = callable.call();
				reset();
				return result;
			} catch (final Exception e) {
				recordFailure();
				throw new CallingException(e);
			}
		case OPEN:
			throw new CircuitBreakerOpenException();
		default:
			throw new AssertionError();
		}
	}

	private void reset() {
		withWriteLock(() -> {
			failureCount = 0;
			lastFailureTime = 0;
			return null;
		});
	}

	private void recordFailure() {
		withWriteLock(() -> {
			failureCount++;
			lastFailureTime = clock.getAsLong();
			return null;
		});
	}

	public State state() {
		final State state = withReadLock(() -> {
			if (failureCount < failureThreshold) {
				return State.CLOSED;
			} else if ((clock.getAsLong() - lastFailureTime) > resetTimeout) {
				return State.HALF_OPEN;
			}
			return null;
		});
		return state != null ? state : State.OPEN;
	}

	private <T> T withWriteLock(Supplier<? extends T> supplier) {
		return withLock(readWriteLock::writeLock, supplier);
	}

	private <T> T withReadLock(Supplier<? extends T> supplier) {
		return withLock(readWriteLock::readLock, supplier);
	}

	private static <T> T withLock(Supplier<? extends Lock> locker, Supplier<? extends T> supplier) {
		final Lock lock = locker.get();
		lock.lock();
		try {
			return supplier.get();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public String toString() {
		return withReadLock(() -> String.format(
				"%s(state = %s, failureCount = %d, lastFailureTime = %d)",
				getClass().getSimpleName(),
				state(),
				failureCount,
				lastFailureTime));
	}
}
