package com.example;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisExample {

	public static void main(String[] args) throws Exception {
		RedisClient redisClient = RedisClient.create("redis://localhost:6379");
		try (StatefulRedisConnection<String, String> connection = redisClient.connect()) {
			RedisCommands<String, String> commands = connection.sync();

			String key = "redis-example";
			String value = UUID.randomUUID().toString();
			Runnable r = () -> {
				System.out.printf("exists=%s, value=%s, ttl=%d(sec)%n",
						commands.exists(key),
						commands.get(key),
						commands.ttl(key));
			};

			r.run();

			System.out.printf("set: %s%n", commands.set(key, value));

			r.run();

			System.out.printf("expire: %s%n", commands.expire(key, 1));

			r.run();

			System.out.printf("sleep 1(sec)%n");
			TimeUnit.SECONDS.sleep(1);

			r.run();
		}
	}
}
