package com.trinity.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

/** A single line of a checkout request. */
public record CheckoutLineItemRequest(
        @NotNull @Positive BigDecimal unitAmount,
        @NotBlank String currency,
        @Positive int quantity,
        @NotBlank String name
) {
}
