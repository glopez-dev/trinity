package com.trinity.product.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

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

    @NotNull
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Embedded
    private NutrientLevels nutrientLevels;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

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

    @Embedded
    private Nutriments nutriments;

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

    private String nutriscoreGrade; 

    @Embedded
    private SelectedImages selectedImages;

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
        private ProductImageUrl display;

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "small_en")),
            @AttributeOverride(name = "fr", column = @Column(name = "small_fr"))
        })
        private ProductImageUrl small;

        @Embedded
        @AttributeOverrides({
            @AttributeOverride(name = "en", column = @Column(name = "thumb_en")),
            @AttributeOverride(name = "fr", column = @Column(name = "thumb_fr"))
        })
        private ProductImageUrl thumb;
    }

    @LastModifiedDate
    private LocalDateTime lastUpdate;

   @Embedded
    private Stock stock;

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