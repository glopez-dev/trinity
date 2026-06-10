package com.trinity.product.domain.exception;

import com.trinity.common.domain.exception.ExternalServiceException;

/**
 * Failure of the external product catalogue (OpenFoodFacts). Part of the
 * {@code ProductCatalogGateway} port contract, hence a domain-level type.
 */
public class ApiException extends ExternalServiceException {
    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
