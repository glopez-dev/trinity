package com.trinity.payment.stripe.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.stripe.model.PaymentIntent;

public class PaymentIntentDTO {
    @JsonRawValue
    private String rawJson;

    public PaymentIntentDTO(PaymentIntent paymentIntent) {
        paymentIntent.getRawJsonObject().toString();
    }
}