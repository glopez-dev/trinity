package com.devops.backend.controllers;

import com.devops.backend.rabbitmq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MessageController {
    private final Sender sender;

    @Autowired
    public MessageController(Sender sender) {
        this.sender = sender;
    }

    @GetMapping("/send")
    public ResponseEntity<String> send() {
        try {
            sender.send("Hello from RabbitMQ!");
            return ResponseEntity.ok("Message sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending message: " + e.getMessage());
        }
    }

    @GetMapping("/health/rabbitmq")
    public ResponseEntity<String> checkRabbitMQ() {
        try {
            sender.send("Health check message");
            return ResponseEntity.ok("RabbitMQ connection OK");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("RabbitMQ connection failed: " + e.getMessage());
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("API is working");
    }
}
