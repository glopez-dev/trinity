package com.trinity.product.dto.api;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "DTO for creating a new product")
public class CreateProductDTO {

    @Schema(description = "Product barcode", example = "3017620422003")
    @NotNull(message = "Barcode cannot be null")
    private String barcode;

    @Schema(description = "Product category", example = "Snacks")
    private String category;

    @Schema(description = "Product brand", example = "Nutella")
    @NotBlank(message = "Brand cannot be blank")
    private String brand;

    @Schema(description = "Product name", example = "Nutella Hazelnut Spread")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Schema(description = "Product ingredients", example = "Sugar, Palm Oil, Hazelnuts, Cocoa")
    private String ingredients;

    @Schema(description = "Product price", example = "4.99")
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private BigDecimal price;

    @Schema(description = "Nutrient levels information")
    private NutrientLevelsDto nutrientLevels;

    @Schema(description = "Detailed nutriments information")
    private NutrimentsDto nutriments;

    @Schema(description = "Nutriscore grade (a-e)", example = "b")
    @Pattern(regexp = "^[a-e]$", message = "Nutriscore grade must be between a and e")
    private String nutriscoreGrade;

    @Schema(description = "Product images in different sizes")
    private SelectedImagesDto selectedImages;

    @Schema(description = "Product stock information")
    @NotNull(message = "Stock cannot be null")
    private StockDto stock;

    @Data
    @Schema(description = "Nutrient levels information")
    public static class NutrientLevelsDto {
        @Schema(description = "Fat level", example = "low")
        private String fat;
        @Schema(description = "Saturated fat level", example = "moderate")
        private String saturatedFat;
        @Schema(description = "Sugars level", example = "high")
        private String sugars;
        @Schema(description = "Salt level", example = "low")
        private String salt;
    }

    @Data
    @Schema(description = "Detailed nutriments information per 100g")
    public static class NutrimentsDto {
        @Schema(description = "Energy in kcal/100g", example = "544.0")
        private Double energyKcal100g;
        @Schema(description = "Proteins in g/100g", example = "6.3")
        private Double proteins100g;
        @Schema(description = "Carbohydrates in g/100g", example = "57.5")
        private Double carbohydrates100g;
        @Schema(description = "Fat in g/100g", example = "31.6")
        private Double fat100g;
        @Schema(description = "Fiber in g/100g", example = "3.4")
        private Double fiber100g;
        @Schema(description = "Salt in g/100g", example = "0.107")
        private Double salt100g;
        @Schema(description = "Sugars in g/100g", example = "56.3")
        private Double sugars100g;
    }

    @Data
    @Schema(description = "Product images in different sizes")
    public static class SelectedImagesDto {
        private DisplayImagesDto display;
        private DisplayImagesDto small;
        private DisplayImagesDto thumb;

        @Data
        @Schema(description = "Image URLs for different languages")
        public static class DisplayImagesDto {
            @Schema(description = "English image URL")
            private String en;
            @Schema(description = "French image URL")
            private String fr;
        }
    }

    @Data
    @Schema(description = "Stock information")
    public static class StockDto {
        @Schema(description = "Current quantity in stock", example = "100")
        private int quantity;
        @Schema(description = "Minimum threshold for stock", example = "10")
        private int minThreshold;
        @Schema(description = "Maximum threshold for stock", example = "1000")
        private int maxThreshold;
    }
}
