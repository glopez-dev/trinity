package com.trinity.common.domain.exception;

/**
 * Thrown when an upstream third-party service fails or misbehaves (maps to HTTP 502).
 * Deliberately outside the {@link DomainException} hierarchy: an upstream outage is an
 * infrastructure failure, not a violation of a domain rule.
 */
public class ExternalServiceException extends RuntimeException {

    public ExternalServiceException(String message) {
        super(message);
    }

    public ExternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
