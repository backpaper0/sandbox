package com.example;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ServerExtension.class)
class MessagingClientTest {

    @Test
    void messaging() throws Exception {
        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        final URI path = URI.create("ws://localhost:8080/messaging");
        final MessagingClient client = new MessagingClient();
        try (Session session = container.connectToServer(client, path)) {

            while (session.isOpen() == false) {
                TimeUnit.MILLISECONDS.sleep(10);
            }

            for (int i = 0; i < 10; i++) {
                TimeUnit.SECONDS.sleep(1);
                session.getBasicRemote().sendText(String.valueOf(i));
            }

        }
    }

    @ClientEndpoint
    public static class MessagingClient {

        @OnOpen
        public void onOpen(final Session session, final EndpointConfig config) {
            System.out.printf("OPEN[%s]%s%n", Thread.currentThread().getName(), session);
        }

        @OnClose
        public void onClose(final Session session, final CloseReason closeReason) {
            System.out.printf("CLOSE[%s]%s%n", Thread.currentThread().getName(), session);
        }

        @OnError
        public void onError(final Session session, final Throwable throwable) {
            System.out.printf("ERROR[%s]%s - %s%n", Thread.currentThread().getName(), session,
                    throwable);
        }

        @OnMessage
        public void onMessage(final Session session, final String message) {
            System.out.printf("MESSAGE[%s]%s - %s%n", Thread.currentThread().getName(), session,
                    message);
        }
    }
}
