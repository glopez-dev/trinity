package com.trinity.common.interfaces.rest;

import java.time.Instant;

/**
 * Uniform error payload returned by {@link GlobalExceptionHandler}.
 */
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
