package com.trinity.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;

import java.util.concurrent.TimeUnit;

public class Runner implements CommandLineRunner {

    private final Receiver receiver;
    private final RabbitTemplate rabbitTemplate;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Waiting for messages...");
        rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE_NAME, "foo.bar.baz", "Hello from RabbitMQ!");
        receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    }
}
