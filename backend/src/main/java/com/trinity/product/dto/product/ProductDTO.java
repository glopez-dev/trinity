package com.trinity.product.dto.product;

import lombok.Data;

@Data
public class ProductDTO {

    private String id;
    private String barcode;
    private String category;
    private String brand;
    private String name;
    private String ingredients;
    private String price;
    private NutrientLevelsDto nutrientLevels;
    private NutrimentsDto nutriments;
    private String nutriscoreGrade;
    private SelectedImagesDto selectedImages;
    private String lastUpdate;
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
        private Integer energyKcal100g;
        private Double proteins100g;
        private Double carbohydrates100g;
        private Integer fat100g;
        private Integer fiber100g;
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
        private String currentQuantity;
        private String minThreshold;
        private String maxThreshold;
    }
}