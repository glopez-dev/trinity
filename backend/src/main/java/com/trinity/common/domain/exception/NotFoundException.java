package com.trinity.common.domain.exception;

/**
 * Thrown when a requested aggregate or entity does not exist (maps to HTTP 404).
 */
public class NotFoundException extends DomainException {

    public NotFoundException(String message) {
        super(message);
    }
}
