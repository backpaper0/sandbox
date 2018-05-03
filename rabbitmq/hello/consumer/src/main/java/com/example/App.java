package com.example;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class App {

    private final static String QUEUE_NAME = "hello";

    public static void main(final String[] args) throws IOException, TimeoutException {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(final String consumerTag, final Envelope envelope,
                    final AMQP.BasicProperties properties,
                    final byte[] body) throws IOException {
                final String message = new String(body, StandardCharsets.UTF_8);
                System.out.println(
                        " [x] Received '" + message + "' " + Thread.currentThread().getName());
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
}
