package com.trinity.product.domain.exception;

import com.trinity.common.domain.exception.BusinessRuleViolation;

public class InvalidProductDataException extends BusinessRuleViolation {
    public InvalidProductDataException(String message) {
        super(message);
    }
}
