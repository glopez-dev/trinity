package com.trinity.payment.domain.model;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.common.domain.vo.Money;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Payment aggregate. A pure domain POJO (no Spring, no JPA, no SDK) holding the
 * lifecycle of a single payment against an external provider. Monetary amounts
 * use the shared immutable {@link Money} value object; transitions are guarded
 * by {@link PaymentStatus}.
 */
@Getter
public class Payment {

    private final UUID id;
    private final Money amount;
    private final PaymentProvider provider;
    private final Instant createdAt;

    private PaymentStatus status;
    private String externalRef;
    private String failureReason;

    private Payment(UUID id, Money amount, PaymentProvider provider, Instant createdAt) {
        this.id = id;
        this.amount = amount;
        this.provider = provider;
        this.createdAt = createdAt;
        this.status = PaymentStatus.PENDING;
    }

    /** Starts a new payment in PENDING state. */
    public static Payment initiate(Money amount, PaymentProvider provider) {
        if (amount == null) {
            throw new IllegalArgumentException("amount must not be null");
        }
        if (provider == null) {
            throw new IllegalArgumentException("provider must not be null");
        }
        return new Payment(UUID.randomUUID(), amount, provider, Instant.now());
    }

    /**
     * Rebuilds a persisted payment. For persistence adapters only: restores the
     * stored state verbatim, bypassing the transition guards.
     */
    public static Payment rehydrate(UUID id, Money amount, PaymentProvider provider, Instant createdAt,
                                    PaymentStatus status, String externalRef, String failureReason) {
        Payment payment = new Payment(id, amount, provider, createdAt);
        payment.status = status;
        payment.externalRef = externalRef;
        payment.failureReason = failureReason;
        return payment;
    }

    /** Attaches the provider session reference to a still-PENDING payment (hosted checkout). */
    public void assignExternalRef(String externalRef) {
        if (externalRef == null || externalRef.isBlank()) {
            throw new BusinessRuleViolation("A provider reference must not be blank");
        }
        if (this.externalRef != null) {
            throw new BusinessRuleViolation("The provider reference is already assigned");
        }
        if (this.status != PaymentStatus.PENDING) {
            throw new BusinessRuleViolation("Only a pending payment can receive its provider reference");
        }
        this.externalRef = externalRef;
    }

    public void markAuthorized(String externalRef) {
        if (externalRef == null || externalRef.isBlank()) {
            throw new BusinessRuleViolation("An authorized payment must carry a provider reference");
        }
        this.status.assertCanTransitionTo(PaymentStatus.AUTHORIZED);
        this.status = PaymentStatus.AUTHORIZED;
        this.externalRef = externalRef;
    }

    public void markSucceeded() {
        this.status.assertCanTransitionTo(PaymentStatus.SUCCEEDED);
        this.status = PaymentStatus.SUCCEEDED;
    }

    public void markFailed(String reason) {
        this.status.assertCanTransitionTo(PaymentStatus.FAILED);
        this.status = PaymentStatus.FAILED;
        this.failureReason = reason;
    }

    public void cancel() {
        this.status.assertCanTransitionTo(PaymentStatus.CANCELLED);
        this.status = PaymentStatus.CANCELLED;
    }
}
