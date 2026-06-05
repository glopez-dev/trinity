package com.trinity.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/** Request body to charge a payment directly with a provider payment-method token. */
public record ChargeRequest(
        @NotNull @Positive BigDecimal amount,
        @NotBlank String currency,
        @NotBlank String paymentMethodToken
) {
}
