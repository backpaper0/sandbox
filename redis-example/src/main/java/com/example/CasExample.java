package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.RedisClient;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * @see <a href="https://redis.io/topics/transactions#optimistic-locking-using-check-and-set">
 *      https://redis.io/topics/transactions#optimistic-locking-using-check-and-set
 *      </a>
 *
 */
public class CasExample implements Runnable {

	public static void main(String[] args) throws Exception {
		String key = "CasExample";
		RedisClient redisClient = RedisClient.create("redis://localhost:6379");
		try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
			RedisCommands<String, String> commands = connection.sync();

			commands.set(key, "0");

			int size = 3;
			ExecutorService executor = Executors.newFixedThreadPool(size);
			try {
				List<Future<?>> futures = new ArrayList<>();
				Runnable task = new CasExample(redisClient, key);
				for (int i = 0; i < size; i++) {
					Future<?> future = executor.submit(task);
					futures.add(future);
				}

				for (Future<?> future : futures) {
					future.get(1, TimeUnit.MINUTES);
				}
			} finally {
				executor.shutdown();
			}

			String value = commands.get(key);
			System.out.println(value);

			commands.del(key);
		}
	}

	private final RedisClient redisClient;
	private final String key;

	public CasExample(RedisClient redisClient, String key) {
		this.redisClient = redisClient;
		this.key = key;
	}

	@Override
	public void run() {
		try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
			RedisCommands<String, String> commands = connection.sync();
			for (int i = 0; i < 100; i++) {
				while (true) {
					commands.watch(key);
					int value = Integer.parseInt(commands.get(key));
					value++;
					commands.multi();
					commands.set(key, String.valueOf(value));
					TransactionResult result = commands.exec();
					commands.unwatch();
					if (result.isEmpty() == false && Objects.equals(result.get(0), "OK")) {
						break;
					}
				}
			}
		}
	}
}
