package com.trinity.payment.stripe.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PaymentRequest {

    @Schema(description = "payment amount", example = "100")
    private Long amount;
    @Schema(description = "currency", example = "usd")
    private String currency;
    @Schema(description = "payment method id", example = "pm_card_visa")
    private String paymentMethodId;

    // Getters and Setters
    public Long getAmount() { return amount; }
    public void setAmount(Long amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentMethodId() { return paymentMethodId; }
    public void setPaymentMethodId(String paymentMethodId) { this.paymentMethodId = paymentMethodId; }
}
