package com.trinity.product.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Persistence embeddable mirroring {@link com.trinity.product.domain.model.ProductImageUrl}.
 * Its two columns are remapped per image size via {@code @AttributeOverride} on
 * {@link ProductEntity.SelectedImages}.
 */
@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageUrlEmbeddable {
    private String en;
    private String fr;
}
