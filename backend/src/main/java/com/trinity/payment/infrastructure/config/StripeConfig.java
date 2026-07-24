package com.trinity.payment.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.stripe.Stripe;
import lombok.extern.slf4j.Slf4j;

import jakarta.annotation.PostConstruct;

/**
 * Wires the Stripe SDK's global API key. The StripePaymentAdapter relies on this
 * static key being set at startup.
 */
@Configuration
@Slf4j
public class StripeConfig {

    @Value("${stripe.api.key:}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
        log.info("Stripe API key configured");
    }
}
