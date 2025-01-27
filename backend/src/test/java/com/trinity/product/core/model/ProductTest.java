package com.trinity.product.core.model;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.trinity.product.core.dto.ImageUrls;


class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
    }

    @Test
    void testGetId() {
        // Given
        UUID id = UUID.randomUUID();
        product.setId(id);

        // When
        UUID result = product.getId();

        // Then
        assertThat(result).isEqualTo(id);
    }

    @Test
    void testGetBarcode() {
        // Given
        String barcode = "1234567890123";
        product.setBarcode(barcode);

        // When
        String result = product.getBarcode();

        // Then
        assertThat(result).isEqualTo(barcode);
    }

    @Test
    void testGetCategory() {
        // Given
        String category = "Beverages";
        product.setCategory(category);

        // When
        String result = product.getCategory();

        // Then
        assertThat(result).isEqualTo(category);
    }

    @Test
    void testGetBrand() {
        // Given
        String brand = "Coca-Cola";
        product.setBrand(brand);

        // When
        String result = product.getBrand();

        // Then
        assertThat(result).isEqualTo(brand);
    }

    @Test
    void testGetName() {
        // Given
        String name = "Coca-Cola Classic";
        product.setName(name);

        // When
        String result = product.getName();

        // Then
        assertThat(result).isEqualTo(name);
    }

    @Test
    void testGetIngredients() {
        // Given
        String ingredients = "Carbonated water, sugar, caramel color, phosphoric acid, natural flavors, caffeine";
        product.setIngredients(ingredients);

        // When
        String result = product.getIngredients();

        // Then
        assertThat(result).isEqualTo(ingredients);
    }

    @Test
    void testGetPrice() {
        // Given
        BigDecimal price = new BigDecimal("1.99");
        product.setPrice(price);

        // When
        BigDecimal result = product.getPrice();

        // Then
        assertThat(result).isEqualTo(price);
    }

    @Test
    void testGetNutrientLevels() {
        // Given
        Product.NutrientLevels nutrientLevels = Product.NutrientLevels.builder()
                .fat("High")
                .saturatedFat("Medium")
                .sugars("High")
                .salt("Low")
                .build();
        product.setNutrientLevels(nutrientLevels);

        // When
        Product.NutrientLevels result = product.getNutrientLevels();

        // Then
        assertThat(result).isEqualTo(nutrientLevels);
    }

    @Test
    void testGetNutriments() {
        // Given
        Product.Nutriments nutriments = Product.Nutriments.builder()
                .energyKcal100g(42.0)
                .proteins100g(0.0)
                .carbohydrates100g(10.6)
                .fat100g(0.0)
                .fiber100g(0.0)
                .salt100g(0.01)
                .sugars100g(10.6)
                .build();
        product.setNutriments(nutriments);

        // When
        Product.Nutriments result = product.getNutriments();

        // Then
        assertThat(result).isEqualTo(nutriments);
    }

    @Test
    void testGetNutriscoreGrade() {
        // Given
        String nutriscoreGrade = "C";
        product.setNutriscoreGrade(nutriscoreGrade);

        // When
        String result = product.getNutriscoreGrade();

        // Then
        assertThat(result).isEqualTo(nutriscoreGrade);
    }

    @Test
    void testGetSelectedImages() {
        // Given
        Product.SelectedImages selectedImages = Product.SelectedImages.builder()
                .display(new ImageUrls())
                .small(new ImageUrls())
                .thumb(new ImageUrls())
                .build();
        product.setSelectedImages(selectedImages);

        // When
        Product.SelectedImages result = product.getSelectedImages();

        // Then
        assertThat(result).isEqualTo(selectedImages);
    }

    @Test
    void testGetLastUpdate() {
        // Given
        LocalDateTime lastUpdate = LocalDateTime.now();
        product.setLastUpdate(lastUpdate);

        // When
        LocalDateTime result = product.getLastUpdate();

        // Then
        assertThat(result).isEqualTo(lastUpdate);
    }
}