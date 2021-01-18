package oreore;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

public class OreoreForkJoin {

	public static void main(final String[] args) {
		System.out.println("オレオレfork joinでフィボナッチ(～ 'ω' )～");

		//シングルスレッドで実行
		test(0);

		//コア数のスレッドで実行
		test(Runtime.getRuntime().availableProcessors() - 1);
	}

	static void test(final int parallelism) {
		final long start = System.currentTimeMillis();

		final Pool pool = new Pool(parallelism);
		final Fibonacci task = new Fibonacci(pool, 10);
		task.fork();
		final Integer result = task.join();

		final long end = System.currentTimeMillis();

		System.out.printf("parallelism=%d result=%d time=%d(msec)%n",
				parallelism, result, end - start);
	}

	static class Fibonacci extends Task<Integer> {

		private final int n;

		public Fibonacci(final Pool pool, final int n) {
			super(pool);
			this.n = n;
		}

		@Override
		protected Integer compute() {
			return calculate(n);
		}

		private int calculate(final int n) {

			//↓このコメント外したらタスクがどのスレッドで実行されてるか確認できる
			//System.out.printf("[%s] %d%n", Thread.currentThread().getName(), n);

			//重たいタスクを擬似的にアレ
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (final InterruptedException e) {
			}

			//フィボナッチ(～ 'ω' )～
			if (n < 2) {
				return n;
			}
			final Fibonacci other = new Fibonacci(pool, n - 1);
			other.fork();
			return calculate(n - 2) + other.join();
		}
	}

	static abstract class Task<T> {

		private T result;
		private final AtomicBoolean done = new AtomicBoolean(false);
		protected final Pool pool;

		public Task(final Pool pool) {
			this.pool = pool;
		}

		public void fork() {
			pool.fork(this);
		}

		public T join() {
			pool.join(done::get);
			return result;
		}

		void execute() {
			result = compute();
			done.set(true);
		}

		protected abstract T compute();
	}

	static class Pool {
		private final BlockingQueue<Task<?>> queue = new LinkedBlockingQueue<>();
		private final Executor executor;

		public Pool(final int parallelism) {
			if (parallelism > 0) {
				executor = Executors.newFixedThreadPool(parallelism, r -> {
					final Thread t = new Thread(r);
					t.setDaemon(true);
					return t;
				});
			} else {
				//並列実行数が0ならmainスレッドだけで頑張るマン
				executor = Runnable::run;
			}
		}

		public void fork(final Task<?> task) {
			queue.offer(task);
			executor.execute(() -> trySteal(queue::isEmpty));
		}

		public void join(final BooleanSupplier condition) {
			trySteal(condition);
		}

		private void trySteal(final BooleanSupplier condition) {
			while (condition.getAsBoolean() == false) {
				final Task<?> task = queue.poll();
				if (task != null) {
					task.execute();
				}
			}
		}
	}
}
