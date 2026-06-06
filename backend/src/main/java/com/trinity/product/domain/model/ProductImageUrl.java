package com.trinity.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain value object holding the localized URLs of a product image. Owned by
 * the product domain — distinct from the OpenFoodFacts wire DTO and from the
 * persistence embeddable, so neither representation leaks into the model.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageUrl {
    private String en;
    private String fr;
}
