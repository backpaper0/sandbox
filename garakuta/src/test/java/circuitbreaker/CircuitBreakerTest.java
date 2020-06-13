package circuitbreaker;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CircuitBreakerTest {

	@Test
	void test() throws Exception {
		final int failureThreshold = 3;
		final long resetTimeout = TimeUnit.SECONDS.toMillis(100);
		final MockClock clock = new MockClock();
		final CircuitBreaker cb = new CircuitBreaker(failureThreshold, resetTimeout, clock);

		final Callable<String> success = () -> "success";
		final Callable<String> failure = () -> {
			throw new Exception("failure");
		};

		for (int i = 0; i < 10; i++) {
			cb.execute(success);
			assertEquals(State.CLOSED, cb.state());
		}

		// First failure
		assertThrows(CallingException.class, () -> cb.execute(failure));
		assertEquals(State.CLOSED, cb.state());

		// Second failure
		assertThrows(CallingException.class, () -> cb.execute(failure));
		assertEquals(State.CLOSED, cb.state());

		// Third failure (circuit breaker will open)
		assertThrows(CallingException.class, () -> cb.execute(failure));
		assertEquals(State.OPEN, cb.state());

		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> cb.execute(success));
			assertEquals(State.OPEN, cb.state());
		}

		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> cb.execute(failure));
			assertEquals(State.OPEN, cb.state());
		}

		//Turn the clock forward (Just before the reset timeout)
		clock.plus(100, TimeUnit.SECONDS);
		assertEquals(State.OPEN, cb.state());

		//Turn the clock forward (Reset failure state)
		clock.plus(1, TimeUnit.SECONDS);
		assertEquals(State.HALF_OPEN, cb.state());

		//If it fails, circuit breaker will reopen
		assertThrows(CallingException.class, () -> cb.execute(failure));
		assertEquals(State.OPEN, cb.state());

		//Turn the clock forward (Reset failure state)
		clock.plus(101, TimeUnit.SECONDS);
		assertEquals(State.HALF_OPEN, cb.state());

		//If successful, circuit breaker will closed
		cb.execute(success);
		assertEquals(State.CLOSED, cb.state());
	}

	@Test
	void concurrent() throws Exception {
		final int failureThreshold = 10;
		final long resetTimeout = TimeUnit.SECONDS.toMillis(100);
		final MockClock clock = new MockClock();
		final CircuitBreaker cb = new CircuitBreaker(failureThreshold, resetTimeout, clock);

		final int concurrency = 10;

		final CountDownLatch ready = new CountDownLatch(concurrency);
		final CountDownLatch go = new CountDownLatch(1);
		final Callable<String> failure = () -> {
			ready.countDown();
			go.await();
			throw new Exception("failure");
		};

		final ExecutorService executor = Executors.newFixedThreadPool(concurrency);
		try {
			for (int times = 0; times < 100; times++) {
				final Collection<Future<String>> futures = new ArrayList<>();
				for (int i = 0; i < concurrency; i++) {
					final Future<String> future = executor.submit(() -> cb.execute(failure));
					futures.add(future);
				}
				ready.await();
				go.countDown();
				for (final Future<String> future : futures) {
					assertThrows(ExecutionException.class, future::get);
				}
				final Stream<Executable> futureGet = futures.stream().map(future -> {
					final Executable executable = () -> assertThrows(ExecutionException.class,
							future::get);
					return executable;
				});
				final Stream<Executable> assertState = Stream
						.of(() -> assertEquals(State.OPEN, cb.state(), cb::toString));
				assertAll(times + " times", Stream.concat(futureGet, assertState));
				clock.plus(resetTimeout + 1, TimeUnit.MILLISECONDS);
				cb.execute(() -> "success");
			}
		} finally {
			executor.shutdown();
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { Integer.MIN_VALUE, -1, 0 })
	void invalidFailureThreshold(int failureThreshold) throws Exception {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new CircuitBreaker(failureThreshold, 100, new MockClock()));
		assertEquals("failureThreshold must be greather than 0", exception.getMessage());
	}

	@ParameterizedTest
	@ValueSource(longs = { Long.MIN_VALUE, -1, 0 })
	void invalidResetTimeout(long resetTimeout) throws Exception {
		final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> new CircuitBreaker(10, resetTimeout, new MockClock()));
		assertEquals("resetTimeout must be greather than 0", exception.getMessage());
	}

	@Test
	void invalidClock() throws Exception {
		final NullPointerException exception = assertThrows(NullPointerException.class,
				() -> new CircuitBreaker(10, 100, null));
		assertEquals("clock is required", exception.getMessage());
	}
}
