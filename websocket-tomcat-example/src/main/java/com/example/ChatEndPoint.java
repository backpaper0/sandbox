package com.example;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/messaging")
public class ChatEndPoint {

    private final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    private void sendText(final Session session, final String text) {
        for (final Session client : clients) {
            if (client != session) {
                client.getAsyncRemote().sendText(text);
            }
        }
    }

    @OnOpen
    public void onOpen(final Session session) {
        clients.add(session);
        sendText(session, String.format("%sが入室しました", session));
    }

    @OnMessage
    public void onMessage(final Session session, final String text) {
        sendText(session, String.format("%s: %s", session, text));
    }

    @OnClose
    public void onClose(final Session session) {
        clients.remove(session);
        sendText(session, String.format("%sが退出しました", session));
    }
}