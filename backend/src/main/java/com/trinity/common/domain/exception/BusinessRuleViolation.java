package com.trinity.common.domain.exception;

/**
 * Thrown when a business invariant or rule is violated (maps to HTTP 409).
 */
public class BusinessRuleViolation extends DomainException {

    public BusinessRuleViolation(String message) {
        super(message);
    }
}
