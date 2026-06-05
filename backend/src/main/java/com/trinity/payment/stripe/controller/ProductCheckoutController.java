package com.trinity.payment.stripe.controller;

import com.trinity.common.domain.vo.Money;
import com.trinity.payment.config.PaymentProperties;
import com.trinity.payment.domain.PaymentLineItem;
import com.trinity.payment.domain.PaymentProvider;
import com.trinity.payment.domain.PaymentResult;
import com.trinity.payment.dto.CheckoutLineItemRequest;
import com.trinity.payment.dto.CheckoutRequest;
import com.trinity.payment.dto.CheckoutResponse;
import com.trinity.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
@Tag(name = "Stripe", description = "Operations related to Stripe")
public class ProductCheckoutController {

    private final PaymentService paymentService;
    private final PaymentProperties properties;

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkoutProducts(@Valid @RequestBody CheckoutRequest request) {
        List<PaymentLineItem> items = request.items().stream()
                .map(this::toLineItem)
                .toList();
        PaymentResult result = paymentService.createCheckout(
                PaymentProvider.STRIPE, items, properties.getSuccessUrl(), properties.getCancelUrl());
        return ResponseEntity.ok(new CheckoutResponse(
                result.status().name(), result.externalRef(), result.redirectUrl()));
    }

    private PaymentLineItem toLineItem(CheckoutLineItemRequest item) {
        return new PaymentLineItem(
                Money.of(item.unitAmount(), item.currency()), item.quantity(), item.name());
    }
}
