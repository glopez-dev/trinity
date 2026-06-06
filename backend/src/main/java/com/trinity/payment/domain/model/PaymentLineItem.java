package com.trinity.payment.domain.model;

import com.trinity.common.domain.vo.Money;

/**
 * A single line of a checkout, expressed in domain vocabulary (Money + quantity)
 * so the gateway adapter — not the service — translates it into SDK params.
 */
public record PaymentLineItem(Money unitAmount, int quantity, String name) {

    public PaymentLineItem {
        if (unitAmount == null) {
            throw new IllegalArgumentException("unitAmount must not be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than zero");
        }
    }
}
