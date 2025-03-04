package com.trinity.payment.paypal.config;

import org.springframework.context.annotation.Configuration;
import com.paypal.base.rest.APIContext;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Configuration
@NoArgsConstructor
@Getter
@Component
public class PaypalConfig {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    public String mode;

    public APIContext getAPIContext() {
        return new APIContext(clientId, clientSecret, mode);
    }
}