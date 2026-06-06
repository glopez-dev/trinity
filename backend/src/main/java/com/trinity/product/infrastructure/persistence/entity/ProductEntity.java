package com.trinity.product.infrastructure.persistence.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Anemic JPA mirror of the {@link com.trinity.product.domain.model.Product}
 * aggregate. Carries no behaviour — it exists only to map the frozen
 * {@code products} table. Reproduces the original schema byte-for-byte
 * (same columns, lengths, precision, embedded overrides, timestamps, NO version).
 */
@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 13)
    private String barcode;

    @Column(length = 100)
    private String category;

    @Column(length = 100)
    private String brand;

    @Column(nullable = false)
    private String name;

    private String ingredients;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Embedded
    private NutrientLevels nutrientLevels;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Embedded
    private Nutriments nutriments;

    private String nutriscoreGrade;

    @Embedded
    private SelectedImages selectedImages;

    // Inert: no @EnableJpaAuditing wiring exists, so this is never auto-written.
    // Kept solely for schema parity with the frozen column. DO NOT add auditing.
    @LastModifiedDate
    private LocalDateTime lastUpdate;

    @Embedded
    private Stock stock;

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NutrientLevels {
        private String fat;
        private String saturatedFat;
        private String sugars;
        private String salt;
    }

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Nutriments {
        private double energyKcal100g;
        private double proteins100g;
        private double carbohydrates100g;
        private double fat100g;
        private double fiber100g;
        private double salt100g;
        private double sugars100g;
    }

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectedImages {

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "display_en")),
            @AttributeOverride(name = "fr", column = @Column(name = "display_fr"))
        })
        private ProductImageUrlEmbeddable display;

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "small_en")),
            @AttributeOverride(name = "fr", column = @Column(name = "small_fr"))
        })
        private ProductImageUrlEmbeddable small;

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "thumb_en")),
            @AttributeOverride(name = "fr", column = @Column(name = "thumb_fr"))
        })
        private ProductImageUrlEmbeddable thumb;
    }

    @Embeddable
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stock {
        private int quantity;
        private int minThreshold;
        private int maxThreshold;
    }
}
