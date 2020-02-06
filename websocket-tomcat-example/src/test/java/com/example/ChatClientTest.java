package com.example;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.junit.jupiter.api.Test;

class ChatClientTest {

    @Test
    void chat() throws Exception {

        final ExecutorService executor = Executors.newFixedThreadPool(3);

        final WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        final URI path = URI.create("ws://localhost:8080/chat");

        final List<Future<Void>> futures = Stream.of("foo", "bar", "baz")
                .map(message -> {
                    final Callable<Void> task = () -> {

                        final ChatClient client = new ChatClient(message);
                        try (Session session = container.connectToServer(client, path)) {

                            client.await();

                            for (int i = 0, j = (int) (Math.random() * 5) + 1; i < j; i++) {

                                TimeUnit.SECONDS.sleep((int) (Math.random() * 5) + 1);

                                client.sendMessage(i);
                            }

                        }
                        return null;
                    };
                    return executor.submit(task);
                })
                .collect(Collectors.toList());

        for (final Future<Void> future : futures) {
            future.get();
        }

        executor.shutdown();
    }

    @ClientEndpoint
    public static class ChatClient {

        private final String message;
        private final CountDownLatch ready = new CountDownLatch(1);
        private Session session;

        public ChatClient(final String message) {
            this.message = message;
        }

        public void sendMessage(final int i) throws IOException {
            session.getBasicRemote().sendText(message + i);
        }

        public void await() throws InterruptedException {
            ready.await();
        }

        @OnOpen
        public void onOpen(final Session session, final EndpointConfig config) {
            this.session = session;
            ready.countDown();
        }

        @OnMessage
        public void onMessage(final Session session, final String message) {
            System.out.printf("[%s]%s%n", session, message);
        }
    }
}
