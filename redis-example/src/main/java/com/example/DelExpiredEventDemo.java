package com.example;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

public class DelExpiredEventDemo {

	public static void main(String[] args) throws Exception {
		RedisClient redisClient = RedisClient.create("redis://localhost:6379");

		CountDownLatch ready = new CountDownLatch(1);
		CountDownLatch shutdown = new CountDownLatch(1);

		new Thread(() -> {
			CountDownLatch latch = new CountDownLatch(3);
			try (StatefulRedisPubSubConnection<String, String> con = redisClient.connectPubSub()) {
				con.addListener(new RedisPubSubAdapter<>() {
					@Override
					public void message(String pattern, String channel, String message) {
						System.out.printf("pattern=%s, channel=%s, message=%s%n", pattern, channel,
								message);
						latch.countDown();
					}
				});
				RedisPubSubCommands<String, String> commands = con.sync();
				commands.psubscribe("__keyevent@*__:del", "__keyevent@*__:expired");

				ready.countDown();

				try {
					latch.await(1, TimeUnit.MINUTES);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				shutdown.countDown();
			}

		}).start();

		ready.await(1, TimeUnit.MINUTES);

		try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
			RedisCommands<String, String> commands = connection.sync();

			commands.set("foo", "Hello, world!");
			commands.del("foo");

			commands.setex("bar", 1, "Hello, world!");

			commands.set("baz", "Hello, world!");
			commands.expire("baz", 2);

		}

		shutdown.await(1, TimeUnit.MINUTES);
	}
}
