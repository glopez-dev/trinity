package com.trinity.product.dto.api;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ReadProductDTO {

    @NotNull(message = "Id cannot be null")
    private UUID id;

    @NotNull(message = "Barcode cannot be null")
    private String barcode;

    private String category;

    private String brand;

    private String name;

    private String ingredients;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private NutrientLevelsDto nutrientLevels;

    private NutrimentsDto nutriments;

    @Pattern(regexp = "^[a-e]$", message = "Nutriscore grade must be between a and e")
    private String nutriscoreGrade;

    private SelectedImagesDto selectedImages;

    @NotNull(message = "Stock cannot be null")
    private StockDto stock;

    @Data
    public static class NutrientLevelsDto {
        private String fat;
        private String saturatedFat;
        private String sugars;
        private String salt;
    }

    @Data
    public static class NutrimentsDto {
        private Double energyKcal100g;
        private Double proteins100g;
        private Double carbohydrates100g;
        private Double fat100g;
        private Double fiber100g;
        private Double salt100g;
        private Double sugars100g;
    }

    @Data
    public static class SelectedImagesDto {
        private DisplayImagesDto display;
        private DisplayImagesDto small;
        private DisplayImagesDto thumb;

        @Data
        public static class DisplayImagesDto {
            private String en;
            private String fr;
        }
    }

    @Data
    public static class StockDto {
        private int quantity;
        private int minThreshold;
        private int maxThreshold;
    }

}
