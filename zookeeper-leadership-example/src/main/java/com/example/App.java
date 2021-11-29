package com.example;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

	private static final Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) throws InterruptedException {
		String id = UUID.randomUUID().toString();
		logger.info("Start {}", id);
		String connectString = "localhost:2181";
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		int sessionTimeoutMs = 5000;
		int connectionTimeoutMs = 5000;
		try (CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, sessionTimeoutMs,
				connectionTimeoutMs, retryPolicy)) {
			client.start();
			String leaderPath = "/example/leader";
			AtomicInteger counter = new AtomicInteger();
			LeaderSelectorListener listener = new LeaderSelectorListenerAdapter() {
				@Override
				public void takeLeadership(CuratorFramework client) throws Exception {
					logger.info("Take leadership: {} - {}", id, counter.incrementAndGet());
					TimeUnit.SECONDS.sleep(3);

					// takeLeadershipメソッドを抜けるとリーダーシップは破棄される
				}
			};
			try (LeaderSelector leaderSelector = new LeaderSelector(client, leaderPath, listener)) {
				leaderSelector.autoRequeue();
				leaderSelector.start();

				TimeUnit.HOURS.sleep(1);
			}
		}
	}
}
