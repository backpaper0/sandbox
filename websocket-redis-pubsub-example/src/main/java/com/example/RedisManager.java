package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.websocket.Session;

import io.lettuce.core.RedisClient;
import io.lettuce.core.pubsub.RedisPubSubAdapter;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

@WebListener
public class RedisManager implements ServletContextListener {

	private static final Logger logger = Logger.getLogger(RedisManager.class.getName());

	private RedisClient redis;
	private StatefulRedisPubSubConnection<String, String> connectionForSub;
	private RedisPubSubCommands<String, String> commandsForSub;
	private StatefulRedisPubSubConnection<String, String> connectionForPub;
	private RedisPubSubCommands<String, String> commandsForPub;

	@Override
	public void contextInitialized(final ServletContextEvent sce) {
		redis = RedisClient.create("redis://localhost:6379");
		connectionForSub = redis.connectPubSub();
		commandsForSub = connectionForSub.sync();
		connectionForPub = redis.connectPubSub();
		commandsForPub = connectionForPub.sync();

		final ServletContext sc = sce.getServletContext();
		final Accessor accessor = new Accessor(sc);
		accessor.setCommands(commandsForPub);
		final List<Session> sessions = Collections.synchronizedList(new ArrayList<>());
		accessor.setSessions(sessions);

		connectionForSub.addListener(new RedisPubSubAdapter<>() {
			@Override
			public void message(final String channel, final String message) {
				logger.info("Receive: channel = " + channel + ", message = " + message);
				if (channel.equals("message")) {
					final int index = message.indexOf(':');
					final String id = message.substring(0, index);
					final String text = message.substring(index + 1);
					for (final Session session : sessions) {
						final Accessor accessor2 = new Accessor(session.getUserProperties());
						final String id2 = accessor2.getId();
						if (id.equals(id2) == false) {
							try {
								logger.info("Send text: id = " + id2 + ", text = " + text);
								session.getBasicRemote().sendText(text);
							} catch (final IOException e) {
								throw new UncheckedIOException(e);
							}
						}
					}
				}
			}
		});
		commandsForSub.subscribe("message");

	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
		final var c1 = connectionForSub;
		final var c2 = connectionForPub;
		try (c1; c2) {
		}
	}
}
