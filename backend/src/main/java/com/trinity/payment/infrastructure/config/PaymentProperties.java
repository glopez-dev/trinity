package com.trinity.payment.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Externalized checkout configuration (replaces the hardcoded Stripe success/
 * cancel URLs). Sensible defaults keep context startup working when the keys
 * are absent (e.g. in tests).
 */
@Component
@ConfigurationProperties(prefix = "payment.checkout")
@Getter
@Setter
public class PaymentProperties {

    private String successUrl = "http://localhost:8080/success";
    private String cancelUrl = "http://localhost:8080/cancel";
    private String defaultCurrency = "USD";
}
