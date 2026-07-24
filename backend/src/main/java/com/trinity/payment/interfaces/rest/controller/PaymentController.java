package com.trinity.payment.interfaces.rest.controller;

import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.interfaces.rest.dto.ChargeRequest;
import com.trinity.payment.interfaces.rest.dto.PaymentResponse;
import com.trinity.payment.application.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stripe")
@RequiredArgsConstructor
@Tag(name = "Stripe", description = "Operations related to Stripe")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody ChargeRequest request) {
        Payment payment = paymentService.charge(
                Money.of(request.amount(), request.currency()),
                PaymentProvider.STRIPE,
                request.paymentMethodToken());
        return ResponseEntity.ok(toResponse(payment));
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId().toString(),
                payment.getExternalRef(),
                payment.getStatus().name(),
                payment.getAmount().amount(),
                payment.getAmount().currency());
    }
}
