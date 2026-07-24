package com.trinity.payment.domain.model;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentTest {

    private Payment pendingPayment() {
        return Payment.initiate(Money.of(new BigDecimal("20.00"), "USD"), PaymentProvider.STRIPE);
    }

    @Test
    void initiate_startsPendingAndKeepsMoney() {
        Payment payment = pendingPayment();

        assertNotNull(payment.getId());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
        assertEquals(PaymentProvider.STRIPE, payment.getProvider());
        assertEquals(new BigDecimal("20.00"), payment.getAmount().amount());
        assertEquals("USD", payment.getAmount().currency());
    }

    @Test
    void markAuthorized_setsStatusAndExternalRef() {
        Payment payment = pendingPayment();

        payment.markAuthorized("pi_123");

        assertEquals(PaymentStatus.AUTHORIZED, payment.getStatus());
        assertEquals("pi_123", payment.getExternalRef());
    }

    @Test
    void markSucceeded_fromAuthorized() {
        Payment payment = pendingPayment();
        payment.markAuthorized("pi_123");

        payment.markSucceeded();

        assertEquals(PaymentStatus.SUCCEEDED, payment.getStatus());
    }

    @Test
    void markSucceeded_fromPending_isRejected() {
        Payment payment = pendingPayment();

        assertThrows(BusinessRuleViolation.class, payment::markSucceeded);
    }

    @Test
    void markAuthorized_rejectsBlankExternalRef() {
        Payment payment = pendingPayment();

        assertThrows(BusinessRuleViolation.class, () -> payment.markAuthorized(null));
        assertThrows(BusinessRuleViolation.class, () -> payment.markAuthorized("  "));
    }

    @Test
    void markFailed_recordsReason() {
        Payment payment = pendingPayment();

        payment.markFailed("card declined");

        assertEquals(PaymentStatus.FAILED, payment.getStatus());
        assertEquals("card declined", payment.getFailureReason());
    }

    @Test
    void cancel_fromPending() {
        Payment payment = pendingPayment();

        payment.cancel();

        assertEquals(PaymentStatus.CANCELLED, payment.getStatus());
    }

    @Test
    void cancel_afterSucceeded_isRejected() {
        Payment payment = pendingPayment();
        payment.markAuthorized("pi_123");
        payment.markSucceeded();

        assertThrows(BusinessRuleViolation.class, payment::cancel);
    }

    @Test
    void rehydrate_restoresPersistedStateVerbatim() {
        java.util.UUID id = java.util.UUID.randomUUID();
        java.time.Instant createdAt = java.time.Instant.parse("2026-01-01T00:00:00Z");

        Payment payment = Payment.rehydrate(id, Money.of(new java.math.BigDecimal("12.00"), "USD"),
                PaymentProvider.STRIPE, createdAt, PaymentStatus.FAILED, "pi_x", "declined");

        org.assertj.core.api.Assertions.assertThat(payment.getId()).isEqualTo(id);
        org.assertj.core.api.Assertions.assertThat(payment.getCreatedAt()).isEqualTo(createdAt);
        org.assertj.core.api.Assertions.assertThat(payment.getStatus()).isEqualTo(PaymentStatus.FAILED);
        org.assertj.core.api.Assertions.assertThat(payment.getExternalRef()).isEqualTo("pi_x");
        org.assertj.core.api.Assertions.assertThat(payment.getFailureReason()).isEqualTo("declined");
    }

    @Test
    void assignExternalRef_onPendingPayment_setsTheRef() {
        Payment payment = Payment.initiate(Money.of(java.math.BigDecimal.TEN, "USD"), PaymentProvider.STRIPE);

        payment.assignExternalRef("cs_123");

        org.assertj.core.api.Assertions.assertThat(payment.getExternalRef()).isEqualTo("cs_123");
        org.assertj.core.api.Assertions.assertThat(payment.getStatus()).isEqualTo(PaymentStatus.PENDING);
    }

    @Test
    void assignExternalRef_rejectsBlankDuplicateOrNonPending() {
        Payment payment = Payment.initiate(Money.of(java.math.BigDecimal.TEN, "USD"), PaymentProvider.STRIPE);

        org.assertj.core.api.Assertions.assertThatExceptionOfType(
                com.trinity.common.domain.exception.BusinessRuleViolation.class)
                .isThrownBy(() -> payment.assignExternalRef(" "));

        payment.assignExternalRef("cs_1");
        org.assertj.core.api.Assertions.assertThatExceptionOfType(
                com.trinity.common.domain.exception.BusinessRuleViolation.class)
                .isThrownBy(() -> payment.assignExternalRef("cs_2"));

        Payment authorized = Payment.initiate(Money.of(java.math.BigDecimal.TEN, "USD"), PaymentProvider.STRIPE);
        authorized.markAuthorized("pi_1");
        org.assertj.core.api.Assertions.assertThatExceptionOfType(
                com.trinity.common.domain.exception.BusinessRuleViolation.class)
                .isThrownBy(() -> authorized.assignExternalRef("cs_3"));
    }
}
