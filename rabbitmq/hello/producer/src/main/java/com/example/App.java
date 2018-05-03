package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class App {

    private final static String QUEUE_NAME = "hello";

    public static void main(final String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            for (int i = 0, size = sizeFromFirstElementOrDefault(args); i < size; i++) {
                final String message = String.format("%s:%s", i, UUID.randomUUID());
                channel.basicPublish("", QUEUE_NAME, null,
                        message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }

    private static int sizeFromFirstElementOrDefault(final String[] args) {
        return Arrays.stream(args)
                .mapToInt(Integer::parseInt)
                .findFirst()
                .orElse(1);
    }
}
