package com.trinity.product.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.LastModifiedDate;

import com.trinity.product.dto.open_food_facts.ImageUrls;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String barcode;

    private String category;

    private String brand;

    @Column(nullable = false)
    private String name;

    private String ingredients;

    @Column(nullable = false)
    private BigDecimal price;

    @Embedded
    private NutrientLevels nutrientLevels;

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
        private ImageUrls display;
 
        @Embedded
        private ImageUrls small;
 
        @Embedded
        private ImageUrls thumb;
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