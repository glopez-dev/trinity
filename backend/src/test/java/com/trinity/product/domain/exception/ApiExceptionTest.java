package com.trinity.product.domain.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.trinity.common.domain.exception.ExternalServiceException;


class ApiExceptionTest {

    @Test
    void testApiExceptionMessageAndCause() {
        // Given
        String message = "Test exception message";
        Throwable cause = new RuntimeException("Cause of the exception");

        // When
        ApiException exception = new ApiException(message, cause);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception).isInstanceOf(ExternalServiceException.class);
    }

    @Test
    void testApiExceptionMessageOnly() {
        // Given
        String message = "Test exception message";

        // When
        ApiException exception = new ApiException(message, null);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception.getCause()).isNull();
    }

    @Test
    void testApiExceptionCauseOnly() {
        // Given
        Throwable cause = new RuntimeException("Cause of the exception");

        // When
        ApiException exception = new ApiException(null, cause);

        // Then
        assertThat(exception.getMessage()).isNull();
        assertThat(exception.getCause()).isEqualTo(cause);
    }
}