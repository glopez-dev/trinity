package com.devops.backend.rabbitmq;

import com.devops.backend.TrinityApplication;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Sender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitmqConfig.TOPIC_EXCHANGE_NAME, "foo.bar.baz", message);
    }
}
