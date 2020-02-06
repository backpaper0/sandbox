package com.example;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatEndPoint {

    private static final Set<Session> clients = Collections.synchronizedSet(new HashSet<>());

    private static void sendText(final Session session, final String text) throws IOException {
        synchronized (clients) {
            for (final Session client : clients) {
                if (client.isOpen() && client != session) {
                    client.getBasicRemote().sendText(text);
                }
            }
        }
    }

    @OnOpen
    public void onOpen(final Session session) throws IOException {
        synchronized (clients) {
            clients.add(session);
            sendText(session, String.format("%sが入室しました", session));
        }
    }

    @OnMessage
    public void onMessage(final Session session, final String text) throws IOException {
        sendText(session, String.format("%s: %s", session, text));
    }

    @OnClose
    public void onClose(final Session session) throws IOException {
        synchronized (clients) {
            clients.remove(session);
            sendText(session, String.format("%sが退出しました", session));
        }
    }
}