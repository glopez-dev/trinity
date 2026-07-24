package com.trinity.payment.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.paypal.base.rest.APIContext;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;


@Configuration
@NoArgsConstructor
@Getter
public class PaypalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    public String mode;

    /**
     * Singleton context shared by all PayPal calls: the SDK caches its OAuth
     * token inside the APIContext, so re-creating one per call forced a
     * re-authentication round-trip every time.
     */
    @Bean
    public APIContext apiContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}
