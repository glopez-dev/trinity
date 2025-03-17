package com.trinity.payment.stripe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.trinity.payment.stripe.dto.PaymentRequest;
import com.trinity.payment.stripe.service.StripePaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/stripe")
@Tag(name = "Stripe ", description = "Operations related to Stripe")
public class PaymentController {
    @Autowired
    private StripePaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPayment(@RequestBody PaymentRequest request) {
        try {
            PaymentIntent paymentIntent = paymentService.createPayment(request.getAmount(), request.getCurrency(), request.getPaymentMethodId());
            
            Map<String, Object> jsonCompatible = StripePaymentService.convertJsonObject(paymentIntent.getRawJsonObject());

            return ResponseEntity.ok(jsonCompatible);
        } catch (StripeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
