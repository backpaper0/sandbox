package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine;

public class CircuitBreakerTest {

	@Test
	void test() throws Exception {

		CircuitBreakerConfig config = CircuitBreakerConfig.custom()
				// サーキットブレーカーをOPENにするか判断の基準となる処理回数
				.slidingWindowSize(4)
				// 処理をslidingWindowSize回実行した場合に何パーセント失敗すればCLOSEDにするか
				.failureRateThreshold(50)
				// OPENからHALF_OPENへ状態遷移するまでの時間
				.waitDurationInOpenState(Duration.ofSeconds(3))
				// HALF_OPEN状態で処理を実行できる回数（この回数を超えるとOPENまたはCLOSEDへ状態遷移する）
				.permittedNumberOfCallsInHalfOpenState(6)
				.build();

		MockClock clock = new MockClock();

		CircuitBreaker circuitBreaker = new CircuitBreakerStateMachine("test", config, clock);

		Runnable success = () -> {
		};
		Runnable failure = () -> {
			throw new MyException();
		};

		Consumer<State> doSuccess = state -> {
			assertAll(
					() -> circuitBreaker.executeRunnable(success),
					() -> assertEquals(state, circuitBreaker.getState()));
		};
		BiConsumer<Class<? extends Exception>, State> doFailure = (exception, state) -> {
			assertAll(
					() -> assertThrows(exception, () -> circuitBreaker.executeRunnable(failure)),
					() -> assertEquals(state, circuitBreaker.getState()));
		};

		// 直近のslidingWindowSize回中、failureRateThresholdパーセントが失敗すればOPENになる。
		// この場合は直近の4回中、50パーセント = 2回失敗すればOPENになる。
		doSuccess.accept(State.CLOSED);
		doSuccess.accept(State.CLOSED);
		doFailure.accept(MyException.class, State.CLOSED);
		doFailure.accept(MyException.class, State.OPEN);

		// OPEN状態だと処理は呼ばれずCallNotPermittedExceptionがスローされる。
		for (int i = 0; i < 100; i++) {
			doFailure.accept(CallNotPermittedException.class, State.OPEN);
		}

		// waitDurationInOpenStateギリギリまで時間を進める。
		clock.plusSeconds(3);
		for (int i = 0; i < 100; i++) {
			doFailure.accept(CallNotPermittedException.class, State.OPEN);
		}

		// waitDurationInOpenStateを越えて時間を進めるとHALF_OPENになり、処理が呼ばれるようになる。
		// permittedNumberOfCallsInHalfOpenState回中、failureRateThresholdパーセント失敗しなければ
		// （つまり、成功すれば）CLOSEDになる。
		// この場合は6回中、50パーセント = 3回失敗しなければCLOSEDになる。
		clock.plusSeconds(1);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.CLOSED);

		// 再びHALF_OPEN状態にする。
		doFailure.accept(MyException.class, State.CLOSED);
		doFailure.accept(MyException.class, State.CLOSED);
		doFailure.accept(MyException.class, State.CLOSED);
		doFailure.accept(MyException.class, State.OPEN);
		clock.plusSeconds(4);

		// permittedNumberOfCallsInHalfOpenState回中、failureRateThresholdパーセント失敗したのでOPENになる。
		doFailure.accept(MyException.class, State.HALF_OPEN);
		doFailure.accept(MyException.class, State.HALF_OPEN);
		doFailure.accept(MyException.class, State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.HALF_OPEN);
		doSuccess.accept(State.OPEN);
	}

	static class MyException extends RuntimeException {
	}

	static class MockClock extends Clock {

		private final Instant base = Instant.now();
		private long secondsToAdd;

		@Override
		public ZoneId getZone() {
			return ZoneId.systemDefault();
		}

		@Override
		public Clock withZone(ZoneId zone) {
			return this;
		}

		@Override
		public Instant instant() {
			return base.plusSeconds(secondsToAdd);
		}

		public void plusSeconds(int secondsToAdd) {
			this.secondsToAdd += secondsToAdd;
		}
	}
}
