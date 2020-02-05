package com.example;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/messaging")
public class MessagingEndPoint {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @OnOpen
    public void onOpen(final Session session) {
        System.out.println("[open] " + session);
        final ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> {
            try {
                session.getBasicRemote().sendText(LocalDateTime.now().toString());
            } catch (final IOException e) {
                throw new UncheckedIOException(e);
            }
        }, 0, 3, TimeUnit.SECONDS);
        session.getUserProperties().put("future", future);
    }

    @OnMessage
    public String onMessage(final String message, final Session session) {
        System.out.println("[" + message + "] " + session);
        return message;
    }

    @OnClose
    public void onClose(final Session session) {
        System.out.println("[close] " + session);
        final ScheduledFuture<?> future = (ScheduledFuture<?>) session.getUserProperties()
                .get("future");
        future.cancel(false);
    }
}