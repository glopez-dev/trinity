package com.trinity.payment.domain;

import com.trinity.common.domain.exception.BusinessRuleViolation;

import java.util.Set;

/**
 * Payment lifecycle with an explicit, validated transition set.
 * PENDING -> AUTHORIZED | FAILED | CANCELLED
 * AUTHORIZED -> SUCCEEDED | FAILED
 * SUCCEEDED, FAILED, CANCELLED are terminal.
 */
public enum PaymentStatus {
    PENDING,
    AUTHORIZED,
    SUCCEEDED,
    FAILED,
    CANCELLED;

    private static final java.util.Map<PaymentStatus, Set<PaymentStatus>> ALLOWED = java.util.Map.of(
            PENDING, Set.of(AUTHORIZED, FAILED, CANCELLED),
            AUTHORIZED, Set.of(SUCCEEDED, FAILED),
            SUCCEEDED, Set.of(),
            FAILED, Set.of(),
            CANCELLED, Set.of()
    );

    public boolean canTransitionTo(PaymentStatus target) {
        return ALLOWED.get(this).contains(target);
    }

    public boolean isTerminal() {
        return ALLOWED.get(this).isEmpty();
    }

    public void assertCanTransitionTo(PaymentStatus target) {
        if (!canTransitionTo(target)) {
            throw new BusinessRuleViolation(
                    "Illegal payment transition: %s -> %s".formatted(this, target));
        }
    }
}
