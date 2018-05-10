package com.example.three;

import java.io.IOException;
import java.util.Arrays;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveLogs {
    private static final String EXCHANGE_NAME = "logs";

    public static void main(final String[] argv) throws Exception {
        final ConnectionFactory factory = new ConnectionFactory();
        //        factory.setHost("localhost");
        final Connection connection = factory.newConnection(
                Arrays.asList(new Address("localhost", 5672), new Address("localhost", 5673)));
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        final String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(final String consumerTag, final Envelope envelope,
                    final AMQP.BasicProperties properties, final byte[] body) throws IOException {
                final String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "' " + queueName);
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
}
