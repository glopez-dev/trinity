package com.trinity.payment.service;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.Payment;
import com.trinity.payment.domain.PaymentLineItem;
import com.trinity.payment.domain.PaymentProvider;
import com.trinity.payment.domain.PaymentResult;
import com.trinity.payment.domain.PaymentStatus;
import com.trinity.payment.port.PaymentGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentGateway stripeGateway;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        when(stripeGateway.provider()).thenReturn(PaymentProvider.STRIPE);
        paymentService = new PaymentService(List.of(stripeGateway));
    }

    @Test
    void charge_appliesGatewayResultToAggregate() {
        when(stripeGateway.charge(any(Payment.class), eq("pm_card_visa")))
                .thenReturn(new PaymentResult("pi_123", PaymentStatus.SUCCEEDED, null, null));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE, "pm_card_visa");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(payment.getExternalRef()).isEqualTo("pi_123");
    }

    @Test
    void charge_failedResult_marksPaymentFailed() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenReturn(new PaymentResult("pi_x", PaymentStatus.FAILED, null, "declined"));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("5.00"), "USD"), PaymentProvider.STRIPE, "pm_x");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        assertThat(payment.getFailureReason()).isEqualTo("declined");
    }

    @Test
    void createCheckout_returnsGatewayResult() {
        when(stripeGateway.createCheckout(any(), any(), any()))
                .thenReturn(new PaymentResult("cs_1", PaymentStatus.PENDING, "https://pay", null));

        PaymentResult result = paymentService.createCheckout(
                PaymentProvider.STRIPE,
                List.of(new PaymentLineItem(Money.of(new BigDecimal("10.00"), "USD"), 1, "Coffee")),
                "https://ok", "https://cancel");

        assertThat(result.redirectUrl()).isEqualTo("https://pay");
    }

    @Test
    void charge_unknownProvider_throws() {
        assertThatThrownBy(() -> paymentService.charge(
                Money.of(BigDecimal.TEN, "USD"), PaymentProvider.PAYPAL, "tok"))
                .isInstanceOf(BusinessRuleViolation.class);
    }
}
