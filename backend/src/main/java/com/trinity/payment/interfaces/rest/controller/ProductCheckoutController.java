package com.trinity.payment.interfaces.rest.controller;

import com.trinity.payment.application.command.CheckoutLine;
import com.trinity.payment.infrastructure.config.PaymentProperties;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;
import com.trinity.payment.interfaces.rest.dto.CheckoutLineItemRequest;
import com.trinity.payment.interfaces.rest.dto.CheckoutRequest;
import com.trinity.payment.interfaces.rest.dto.CheckoutResponse;
import com.trinity.payment.application.PaymentService;
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
        List<CheckoutLine> lines = request.items().stream()
                .map(item -> new CheckoutLine(item.productId(), item.quantity()))
                .toList();
        PaymentResult result = paymentService.checkoutProducts(
                PaymentProvider.STRIPE, lines, properties.getDefaultCurrency(),
                properties.getSuccessUrl(), properties.getCancelUrl());
        return ResponseEntity.ok(new CheckoutResponse(
                result.status().name(), result.externalRef(), result.redirectUrl()));
    }
}
