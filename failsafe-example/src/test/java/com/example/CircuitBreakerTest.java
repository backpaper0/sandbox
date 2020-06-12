package com.example;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import net.jodah.failsafe.CircuitBreaker;
import net.jodah.failsafe.CircuitBreaker.State;
import net.jodah.failsafe.CircuitBreakerOpenException;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.FailsafeException;
import net.jodah.failsafe.FailsafeExecutor;
import net.jodah.failsafe.function.CheckedSupplier;

class CircuitBreakerTest {

	private final CheckedSupplier<String> success = () -> "success";
	private final CheckedSupplier<String> failure = () -> {
		throw new Exception("failure");
	};

	@Test
	void test1() throws Exception {
		final CircuitBreaker<String> cb = new CircuitBreaker<String>()
				//処理が2回失敗するとOPENになる
				.withFailureThreshold(2)
				//100ミリ秒はOPENのまま
				.withDelay(Duration.ofMillis(100))
				//処理が2回成功するとCLOSEDになる
				.withSuccessThreshold(2);

		final FailsafeExecutor<String> f = Failsafe.with(cb);

		//最初はCLOSED
		assertEquals(State.CLOSED, cb.getState());

		//処理が成功している間はCLOSED
		for (int i = 0; i < 10; i++) {
			f.get(success);
			assertEquals(State.CLOSED, cb.getState());
		}

		//1回目の失敗ではCLOSEDのまま
		//なお、処理が失敗するとFailsafeExceptionがスローされる
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.CLOSED, cb.getState());

		//2回目の失敗でOPENになる(withFailureThresholdで閾値を2に設定しているため)
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.OPEN, cb.getState());

		//withDelayで設定された期間中はたとえ処理が成功しても
		//CircuitBreakerOpenExceptionがスローされる
		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> f.get(success));
			assertEquals(State.OPEN, cb.getState());
		}

		//もちろん処理が失敗してもCircuitBreakerOpenExceptionがスローされる
		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> f.get(failure));
			assertEquals(State.OPEN, cb.getState());
		}

		//withDelayで設定された期間が過ぎるまで待つ
		//(ここでは少し多めにsleepしておくことにする)
		TimeUnit.MILLISECONDS.sleep(200);

		//HALF_OPENになったはずだけど、まだOPENのまま
		//どうやら何かしらの処理を開始しないと状態の更新はされないらしい
		assertEquals(State.OPEN, cb.getState());

		//1回目の成功ではHALF_OPEN
		f.get(success);
		assertEquals(State.HALF_OPEN, cb.getState());

		//2回目の成功でCLOSEDになる(withSuccessThresholdで閾値を2に設定しているため)
		f.get(success);
		assertEquals(State.CLOSED, cb.getState());
	}

	@Test
	void test2() throws Exception {
		final CircuitBreaker<String> cb = new CircuitBreaker<String>()
				//処理が直近3回のうち2回失敗するとOPENになる
				.withFailureThreshold(2, 3)
				//100ミリ秒はOPENのまま
				.withDelay(Duration.ofMillis(100))
				//処理が直近3回のうち2回成功するとCLOSEDになる
				.withSuccessThreshold(2, 3);

		final FailsafeExecutor<String> f = Failsafe.with(cb);

		//最初はCLOSED
		assertEquals(State.CLOSED, cb.getState());

		//処理が成功している間はCLOSED
		for (int i = 0; i < 10; i++) {
			f.get(success);
			assertEquals(State.CLOSED, cb.getState());
		}

		//1回目の失敗ではCLOSEDのまま
		//なお、処理が失敗するとFailsafeExceptionがスローされる
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.CLOSED, cb.getState());

		//2回成功した後に1回失敗する
		//直近3回のうち1回の失敗となるためCLOSEDのまま
		f.get(success);
		f.get(success);
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.CLOSED, cb.getState());

		//ここでもう1回失敗すると直近3回のうち2回の失敗になるためOPENになる
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.OPEN, cb.getState());

		//withDelayで設定された期間中はtest1と同じ動作
		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> f.get(success));
			assertEquals(State.OPEN, cb.getState());
		}
		for (int i = 0; i < 10; i++) {
			assertThrows(CircuitBreakerOpenException.class, () -> f.get(failure));
			assertEquals(State.OPEN, cb.getState());
		}

		TimeUnit.MILLISECONDS.sleep(200); //待つ
		assertEquals(State.OPEN, cb.getState()); //HALF_OPENのはずなのにOPENが返ってくる仕様

		//1回目の成功ではHALF_OPEN
		f.get(success);
		assertEquals(State.HALF_OPEN, cb.getState());
		//HALF_OPENになると失敗回数はリセットされる
		assertEquals(0, cb.getFailureCount());

		//1回失敗しただけだとHALF_OPENのまま
		assertThrows(FailsafeException.class, () -> f.get(failure));
		assertEquals(State.HALF_OPEN, cb.getState());

		//ここで1回成功すると直近3回のうち2回の成功となるためCLOSEDになる
		//(仮にここでもう1回失敗すると直近3回のうち2回の失敗となるためまたOPENになってしまう)
		f.get(success);
		assertEquals(State.CLOSED, cb.getState());
	}
}
