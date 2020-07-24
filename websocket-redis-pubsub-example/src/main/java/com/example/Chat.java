package com.example;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.HandshakeResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.example.Chat.NotifierConfigurator;

@ServerEndpoint(value = "/chat", configurator = NotifierConfigurator.class)
public class Chat {

	private static final Logger logger = Logger.getLogger(Chat.class.getName());

	private Accessor accessor;

	@OnOpen
	public void onOpen(final Session session) {
		final String id = UUID.randomUUID().toString();
		session.setMaxIdleTimeout(TimeUnit.HOURS.toMillis(1));
		accessor = new Accessor(session.getUserProperties());
		accessor.setId(id);
		logger.info("Open WebSocket: id = " + id);
		final List<Session> sessions = accessor.getSessions();
		sessions.add(session);
	}

	@OnClose
	public void onClose(final Session session, final CloseReason reason) {
		logger.info("Close WebSocket: id = " + accessor.getId());
		final List<Session> sessions = accessor.getSessions();
		sessions.remove(session);
	}

	@OnMessage
	public void onMessage(final Session session, final String text) {
		if (text.isBlank() == false) {
			final String message = accessor.getId() + ":" + text;
			logger.info("Publis: message = " + message);
			accessor.getCommands().publish("message", message);
		}
	}

	public static class NotifierConfigurator extends ServerEndpointConfig.Configurator {

		@Override
		public void modifyHandshake(final ServerEndpointConfig sec, final HandshakeRequest request,
				final HandshakeResponse response) {
			final HttpSession httpSession = (HttpSession) request.getHttpSession();
			final ServletContext sc = httpSession.getServletContext();
			final Accessor from = new Accessor(sc);
			final Accessor to = new Accessor(sec.getUserProperties());
			to.setCommands(from.getCommands());
			to.setSessions(from.getSessions());
		}
	}
}
