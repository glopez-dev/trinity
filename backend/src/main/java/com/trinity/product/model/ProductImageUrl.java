package com.trinity.product.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain value object holding the localized URLs of a product image.
 * Owned by the product domain — distinct from the OpenFoodFacts DTO so the
 * external representation never leaks into the persisted model.
 */
@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageUrl {
    private String en;
    private String fr;
}
