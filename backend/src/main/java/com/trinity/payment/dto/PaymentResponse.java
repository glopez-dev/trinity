package com.trinity.payment.dto;

import java.math.BigDecimal;

/** Clean API representation of a Payment — never exposes an SDK object. */
public record PaymentResponse(
        String paymentId,
        String externalRef,
        String status,
        BigDecimal amount,
        String currency
) {
}
