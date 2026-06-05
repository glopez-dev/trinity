package com.trinity.payment.domain;

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

    public void markAuthorized(String externalRef) {
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
