package com.trinity.product.infrastructure.external.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pure OpenFoodFacts JSON DTO for image URLs. Translated into the domain
 * {@link com.trinity.product.domain.model.ProductImageUrl} value object by the adapter.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageUrls {
    private String en;
    private String fr;
}