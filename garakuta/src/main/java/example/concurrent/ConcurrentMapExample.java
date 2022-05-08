package example.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ConcurrentMapExample {

	public static void main(String[] args) throws Exception {
		/*
		 * ConcurrentHashMap#computeIfAbsentはキー毎にアトミックに実行されるため
		 * 簡易的なキャッシュに使える。
		 */
		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

		ExecutorService executor = Executors.newFixedThreadPool(4);
		try {

			String key1 = "foo";
			String key2 = "bar";
			CountDownLatch latch = new CountDownLatch(1);

			List<Future<String>> futures = new ArrayList<>();
			futures.add(executor.submit(() -> map.computeIfAbsent(key1, key -> {
				try {
					latch.await(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return "foo1";
			})));

			Consumer<String> consumer = value -> futures
					.add(executor.submit(() -> map.computeIfAbsent(key1, key -> value)));
			consumer.accept("foo2");
			consumer.accept("foo3");
			consumer.accept("foo4");
			consumer.accept("foo5");
			consumer.accept("foo6");
			consumer.accept("foo7");
			consumer.accept("foo8");
			consumer.accept("foo9");

			System.out.println(map.computeIfAbsent(key2, key -> "bar"));

			latch.countDown();

			for (Future<String> future : futures) {
				System.out.println(future.get(10, TimeUnit.SECONDS));
			}

		} finally {
			executor.shutdown();
		}
	}
}
