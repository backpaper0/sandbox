package com.example;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.servlet.ServletContext;
import javax.websocket.Session;

import io.lettuce.core.pubsub.api.sync.RedisPubSubCommands;

public class Accessor {

	private final Function<String, Object> getter;
	private final BiConsumer<String, Object> setter;

	public Accessor(final Function<String, Object> getter,
			final BiConsumer<String, Object> setter) {
		this.getter = getter;
		this.setter = setter;
	}

	public Accessor(final Map<String, Object> map) {
		this(map::get, map::put);
	}

	public Accessor(final ServletContext sc) {
		this(sc::getAttribute, sc::setAttribute);
	}

	public String getId() {
		return (String) getter.apply("id");
	}

	public void setId(final String id) {
		setter.accept("id", id);
	}

	public RedisPubSubCommands<String, String> getCommands() {
		return (RedisPubSubCommands<String, String>) getter.apply("commands");
	}

	public void setCommands(final RedisPubSubCommands<String, String> commands) {
		setter.accept("commands", commands);
	}

	public List<Session> getSessions() {
		return (List<Session>) getter.apply("sessions");
	}

	public void setSessions(final List<Session> sessions) {
		setter.accept("sessions", sessions);
	}
}
