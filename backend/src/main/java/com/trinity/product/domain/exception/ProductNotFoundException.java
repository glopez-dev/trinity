package com.trinity.product.domain.exception;

import com.trinity.common.domain.exception.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
