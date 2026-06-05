package com.trinity.payment.domain;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PaymentStatusTest {

    @Test
    void pending_canTransitionToAuthorizedFailedCancelled() {
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.AUTHORIZED));
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.FAILED));
        assertTrue(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.CANCELLED));
    }

    @Test
    void pending_cannotJumpToSucceeded() {
        assertFalse(PaymentStatus.PENDING.canTransitionTo(PaymentStatus.SUCCEEDED));
    }

    @Test
    void authorized_canTransitionToSucceededFailed() {
        assertTrue(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.SUCCEEDED));
        assertTrue(PaymentStatus.AUTHORIZED.canTransitionTo(PaymentStatus.FAILED));
    }

    @Test
    void terminalStates_haveNoTransitions() {
        assertTrue(PaymentStatus.SUCCEEDED.isTerminal());
        assertTrue(PaymentStatus.FAILED.isTerminal());
        assertTrue(PaymentStatus.CANCELLED.isTerminal());
        assertFalse(PaymentStatus.PENDING.isTerminal());
        assertFalse(PaymentStatus.AUTHORIZED.isTerminal());
    }

    @Test
    void assertCanTransitionTo_rejectsIllegalTransition() {
        assertThrows(BusinessRuleViolation.class,
                () -> PaymentStatus.SUCCEEDED.assertCanTransitionTo(PaymentStatus.AUTHORIZED));
        assertThrows(BusinessRuleViolation.class,
                () -> PaymentStatus.CANCELLED.assertCanTransitionTo(PaymentStatus.SUCCEEDED));
    }

    @Test
    void assertCanTransitionTo_allowsLegalTransition() {
        // does not throw
        PaymentStatus.PENDING.assertCanTransitionTo(PaymentStatus.AUTHORIZED);
        PaymentStatus.AUTHORIZED.assertCanTransitionTo(PaymentStatus.SUCCEEDED);
    }
}
