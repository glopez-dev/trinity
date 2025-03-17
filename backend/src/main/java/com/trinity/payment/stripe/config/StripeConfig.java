package com.trinity.payment.stripe.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.stripe.Stripe;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.PostConstruct;
import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
@Component
@Slf4j
public class StripeConfig {
    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        log.info("✅ Stripe API configurée !");
    }
}
