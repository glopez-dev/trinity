package com.trinity.payment.stripe.controller;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.Payment;
import com.trinity.payment.domain.PaymentProvider;
import com.trinity.payment.dto.ChargeRequest;
import com.trinity.payment.dto.PaymentResponse;
import com.trinity.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @Test
    void createPayment_returnsCleanPaymentResponse() {
        ChargeRequest request = new ChargeRequest(new BigDecimal("10.00"), "USD", "pm_123");

        Payment payment = Payment.initiate(Money.of(new BigDecimal("10.00"), "USD"), PaymentProvider.STRIPE);
        payment.markAuthorized("pi_123");
        payment.markSucceeded();
        when(paymentService.charge(any(Money.class), eq(PaymentProvider.STRIPE), eq("pm_123")))
                .thenReturn(payment);

        ResponseEntity<PaymentResponse> response = paymentController.createPayment(request);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        PaymentResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.externalRef()).isEqualTo("pi_123");
        assertThat(body.status()).isEqualTo("SUCCEEDED");
        assertThat(body.amount()).isEqualByComparingTo("10.00");
        assertThat(body.currency()).isEqualTo("USD");
        // no Stripe raw-JSON keys leak through the typed record
    }

    @Test
    void createPayment_propagatesGatewayFailureToHandler() {
        ChargeRequest request = new ChargeRequest(new BigDecimal("10.00"), "USD", "pm_declined");
        when(paymentService.charge(any(Money.class), eq(PaymentProvider.STRIPE), eq("pm_declined")))
                .thenThrow(new BusinessRuleViolation("Stripe charge failed: declined"));

        // The controller no longer swallows errors into a Map; it propagates to GlobalExceptionHandler.
        assertThatThrownBy(() -> paymentController.createPayment(request))
                .isInstanceOf(BusinessRuleViolation.class);
    }
}
