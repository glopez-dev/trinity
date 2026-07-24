package com.trinity.product.infrastructure.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    /**
     * The OpenFoodFacts calls are blocking from the caller's point of view
     * (.block() in the adapter), so without timeouts a slow upstream would pin
     * a request thread indefinitely.
     */
    @Bean
    public WebClient webClient(
            @Value("${product.catalog.connect-timeout-ms:5000}") int connectTimeoutMs,
            @Value("${product.catalog.response-timeout-ms:10000}") int responseTimeoutMs) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeoutMs)
                .responseTimeout(Duration.ofMillis(responseTimeoutMs));
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
