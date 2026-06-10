package com.trinity.product.application.command;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Partial update: an empty Optional means "leave the field unchanged". Nulls
 * coming from the REST layer are normalized so the service never null-checks.
 */
public record UpdateProductCommand(
        Optional<String> name,
        Optional<BigDecimal> price,
        Optional<Integer> quantity
) {
    public UpdateProductCommand {
        name = name == null ? Optional.empty() : name;
        price = price == null ? Optional.empty() : price;
        quantity = quantity == null ? Optional.empty() : quantity;
    }
}
