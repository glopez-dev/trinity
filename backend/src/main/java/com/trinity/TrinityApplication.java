package com.trinity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class TrinityApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrinityApplication.class, args);
    }
}