package com.example;

import java.io.IOException;
import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;

import com.example.WebSocketEndpoint.WebSocketEndpointConfigurator;

@ServerEndpoint(value = "/ws", configurator = WebSocketEndpointConfigurator.class)
public class WebSocketEndpoint {

	@OnOpen
	public void onOpen(Session session) throws IOException {
		final List<String> values = session.getRequestParameterMap().get("token");
		if (values.isEmpty() || values.size() > 1) {
			session.close(new CloseReason(CloseCodes.CANNOT_ACCEPT, "Illegal parameter"));
			return;
		}
		final String token = values.get(0);
		final String user = Messages.getAndRemove(token);
		if (user == null) {
			session.close(new CloseReason(CloseCodes.CANNOT_ACCEPT, "Invalid token"));
			return;
		}
		session.getUserProperties().put("user", user);
	}

	@OnMessage
	public void onMessage(final Session session, final String text) {
		final String user = (String) session.getUserProperties().get("user");
		session.getAsyncRemote().sendText(user + ": " + text);
	}

	public static class WebSocketEndpointConfigurator extends ServerEndpointConfig.Configurator {

		private static final WebSocketEndpoint endpoint = new WebSocketEndpoint();

		@Override
		public <T> T getEndpointInstance(Class<T> endpointClass) throws InstantiationException {
			if (endpointClass != WebSocketEndpoint.class) {
				throw new InstantiationException();
			}
			return endpointClass.cast(endpoint);
		}
	}
}
