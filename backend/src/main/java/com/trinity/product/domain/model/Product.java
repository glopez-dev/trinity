package com.trinity.product.domain.model;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Pure domain aggregate for a product. Holds the catalogue behaviour
 * ({@link #changePrice}, {@link #adjustStock} and the {@link Stock} value object)
 * with no persistence concerns: the JPA mapping lives in an anemic mirror entity
 * under infrastructure, joined by a hand-written persistence mapper.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private UUID id;
    private String barcode;
    private String category;
    private String brand;
    private String name;
    private String ingredients;
    private BigDecimal price;
    private NutrientLevels nutrientLevels;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Nutriments nutriments;
    private String nutriscoreGrade;
    private SelectedImages selectedImages;
    private LocalDateTime lastUpdate;
    private Stock stock;

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

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Nutriments {
        // Primitive double: an absent nutriment is the value 0.0, never null.
        private double energyKcal100g;
        private double proteins100g;
        private double carbohydrates100g;
        private double fat100g;
        private double fiber100g;
        private double salt100g;
        private double sugars100g;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SelectedImages {
        private ProductImageUrl display;
        private ProductImageUrl small;
        private ProductImageUrl thumb;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Stock {
        private int quantity;
        private int minThreshold;
        private int maxThreshold;

        public void reduceBy(int amount) {
            if (amount < 0) {
                throw new BusinessRuleViolation("Reduction amount must not be negative");
            }
            if (amount > this.quantity) {
                throw new BusinessRuleViolation("Stock quantity cannot become negative");
            }
            this.quantity -= amount;
        }

        public void increaseBy(int amount) {
            if (amount < 0) {
                throw new BusinessRuleViolation("Increase amount must not be negative");
            }
            this.quantity += amount;
        }

        public boolean isBelowThreshold() {
            return this.quantity < this.minThreshold;
        }
    }

    /* Domain behavior */

    public void changePrice(BigDecimal newPrice) {
        if (newPrice == null) {
            throw new BusinessRuleViolation("Price must not be null");
        }
        if (newPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleViolation("Price cannot be negative");
        }
        this.price = newPrice;
    }

    /** Defaults applied when importing a catalogue product (no price/stock on import). */
    public void applyImportDefaults() {
        if (this.price == null) {
            this.price = new BigDecimal("0.00");
        }
        if (this.stock == null) {
            this.stock = Stock.builder().quantity(0).minThreshold(5).maxThreshold(100).build();
        }
    }

    /** Applies a signed delta to the stock, rejecting a resulting negative quantity. */
    public void adjustStock(int delta) {
        if (this.stock == null) {
            this.stock = Stock.builder().build();
        }
        if (delta >= 0) {
            this.stock.increaseBy(delta);
        } else {
            this.stock.reduceBy(-delta);
        }
    }
}
