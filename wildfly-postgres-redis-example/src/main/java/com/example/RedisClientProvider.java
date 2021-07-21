package com.example;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

@ApplicationScoped
public class RedisClientProvider {

	@Produces
	@ApplicationScoped
	public RedisClient redisClient() {
		return RedisClient.create("redis://cache.example.com:6379");
	}

	@Produces
	@ApplicationScoped
	public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
		return redisClient.connect();
	}

	public void closeRedisConnection(@Disposes StatefulRedisConnection<String, String> con) {
		con.close();
	}

	@Produces
	@ApplicationScoped
	public RedisCommands<String, String> redisCommands(
			StatefulRedisConnection<String, String> con) {
		return con.sync();
	}
}
