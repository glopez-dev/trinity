package com.trinity.common.domain.exception;

/**
 * Base type for all domain-level exceptions. Carries no framework dependency so it
 * can be thrown from the pure domain layer.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }
}
