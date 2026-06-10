package com.trinity.payment.application;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import com.trinity.payment.domain.model.Payment;
import com.trinity.payment.domain.model.PaymentLineItem;
import com.trinity.payment.domain.model.PaymentProvider;
import com.trinity.payment.domain.model.PaymentResult;
import com.trinity.payment.domain.model.PaymentStatus;
import com.trinity.payment.domain.port.PaymentGateway;
import com.trinity.payment.domain.port.PaymentRepositoryPort;
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
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.InOrder;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentGateway stripeGateway;

    @Mock
    private PaymentRepositoryPort paymentRepository;

    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        when(stripeGateway.provider()).thenReturn(PaymentProvider.STRIPE);
        paymentService = new PaymentService(List.of(stripeGateway), paymentRepository);
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
    void charge_cancelledResult_marksPaymentCancelled() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenReturn(new PaymentResult("pi_c", PaymentStatus.CANCELLED, null, null));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("5.00"), "USD"), PaymentProvider.STRIPE, "pm_x");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.CANCELLED);
    }

    @Test
    void charge_pendingResult_leavesPaymentPending() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenReturn(new PaymentResult("pi_p", PaymentStatus.PENDING, null, null));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("5.00"), "USD"), PaymentProvider.STRIPE, "pm_x");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void charge_authorizedResult_marksPaymentAuthorized() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenReturn(new PaymentResult("pi_a", PaymentStatus.AUTHORIZED, null, null));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("5.00"), "USD"), PaymentProvider.STRIPE, "pm_x");

        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.AUTHORIZED);
        assertThat(payment.getExternalRef()).isEqualTo("pi_a");
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

    @Test
    void charge_persistsPendingBeforeTheGateway_andTheOutcomeAfter() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenReturn(new PaymentResult("pi_123", PaymentStatus.SUCCEEDED, null, null));

        Payment payment = paymentService.charge(
                Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE, "pm_card_visa");

        InOrder order = inOrder(paymentRepository, stripeGateway);
        order.verify(paymentRepository).save(payment);          // PENDING trace before the network call
        order.verify(stripeGateway).charge(any(Payment.class), eq("pm_card_visa"));
        order.verify(paymentRepository).save(payment);          // final state after
    }

    @Test
    void charge_gatewayException_persistsFailureAndRethrows() {
        when(stripeGateway.charge(any(Payment.class), any()))
                .thenThrow(new BusinessRuleViolation("stripe down"));

        assertThatThrownBy(() -> paymentService.charge(
                Money.of(new BigDecimal("5.00"), "USD"), PaymentProvider.STRIPE, "pm_x"))
                .isInstanceOf(BusinessRuleViolation.class);

        verify(paymentRepository, org.mockito.Mockito.times(2)).save(any(Payment.class));
    }

    @Test
    void createCheckout_persistsThePaymentWithTheSessionRef() {
        when(stripeGateway.createCheckout(any(), any(), any()))
                .thenReturn(new PaymentResult("cs_1", PaymentStatus.PENDING, "https://pay", null));

        paymentService.createCheckout(
                PaymentProvider.STRIPE,
                List.of(new PaymentLineItem(Money.of(new BigDecimal("10.00"), "USD"), 2, "Coffee")),
                "https://ok", "https://cancel");

        org.mockito.ArgumentCaptor<Payment> captor = org.mockito.ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(captor.capture());
        assertThat(captor.getValue().getExternalRef()).isEqualTo("cs_1");
        assertThat(captor.getValue().getAmount()).isEqualTo(Money.of(new BigDecimal("20.00"), "USD"));
        assertThat(captor.getValue().getStatus()).isEqualTo(PaymentStatus.PENDING);
    }
}
