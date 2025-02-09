package com.trinity.product.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OpenFoodFactsProductTest {

    private OpenFoodFactsProduct product;

    @BeforeEach
    void setUp() {
        product = new OpenFoodFactsProduct();
    }

    @Test
    void testGetAndSetAllergens() {
        // Given
        String allergens = "Peanuts";

        // When
        product.setAllergens(allergens);

        // Then
        assertThat(product.getAllergens()).isEqualTo(allergens);
    }

    @Test
    void testGetAndSetBrands() {
        // Given
        String brands = "BrandA";

        // When
        product.setBrands(brands);

        // Then
        assertThat(product.getBrands()).isEqualTo(brands);
    }

    @Test
    void testGetAndSetCode() {
        // Given
        String code = "123456789";

        // When
        product.setCode(code);

        // Then
        assertThat(product.getCode()).isEqualTo(code);
    }

    @Test
    void testGetAndSetComparedToCategory() {
        // Given
        String comparedToCategory = "Snacks";

        // When
        product.setComparedToCategory(comparedToCategory);

        // Then
        assertThat(product.getComparedToCategory()).isEqualTo(comparedToCategory);
    }

    @Test
    void testGetAndSetGenericNameFr() {
        // Given
        String genericNameFr = "Nom générique";

        // When
        product.setGenericNameFr(genericNameFr);

        // Then
        assertThat(product.getGenericNameFr()).isEqualTo(genericNameFr);
    }

    @Test
    void testGetAndSetGenericNameEn() {
        // Given
        String genericNameEn = "Generic Name";

        // When
        product.setGenericNameEn(genericNameEn);

        // Then
        assertThat(product.getGenericNameEn()).isEqualTo(genericNameEn);
    }

    @Test
    void testGetAndSetIngredientsTextFr() {
        // Given
        String ingredientsTextFr = "Ingrédients en français";

        // When
        product.setIngredientsTextFr(ingredientsTextFr);

        // Then
        assertThat(product.getIngredientsTextFr()).isEqualTo(ingredientsTextFr);
    }

    @Test
    void testGetAndSetIngredientsTextEn() {
        // Given
        String ingredientsTextEn = "Ingredients in English";

        // When
        product.setIngredientsTextEn(ingredientsTextEn);

        // Then
        assertThat(product.getIngredientsTextEn()).isEqualTo(ingredientsTextEn);
    }

    @Test
    void testGetAndSetNutrientLevels() {
        // Given
        OpenFoodFactsNutrientLevels nutrientLevels = new OpenFoodFactsNutrientLevels();
        nutrientLevels.setFat("10g");
        nutrientLevels.setSalt("1g");
        nutrientLevels.setSaturatedFat("5g");
        nutrientLevels.setSugars("15g");

        // When
        product.setNutrientLevels(nutrientLevels);

        // Then
        assertThat(product.getNutrientLevels()).isEqualTo(nutrientLevels);
    }

    @Test
    void testGetAndSetNutriments() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        nutriments.setEnergyKcal(200);
        nutriments.setEnergyKcal100g(100);
        nutriments.setProteins(10);
        nutriments.setProteins100g(5);
        nutriments.setCarbohydrates(30);
        nutriments.setCarbohydrates100g(15);
        nutriments.setFat(20);
        nutriments.setFat100g(10);
        nutriments.setFiber(5);
        nutriments.setFiber100g(2.5);
        nutriments.setSalt(1);
        nutriments.setSalt100g(0.5);
        nutriments.setSugars(15);
        nutriments.setSugars100g(7.5);

        // When
        product.setNutriments(nutriments);

        // Then
        assertThat(product.getNutriments()).isEqualTo(nutriments);
    }

    @Test
    void testGetAndSetNutriscoreGrade() {
        // Given
        String nutriscoreGrade = "A";

        // When
        product.setNutriscoreGrade(nutriscoreGrade);

        // Then
        assertThat(product.getNutriscoreGrade()).isEqualTo(nutriscoreGrade);
    }

    @Test
    void testGetAndSetSelectedImages() {
        // Given
        OpenFoodFactsSelectedImages selectedImages = new OpenFoodFactsSelectedImages();
        Front front = new Front();
        ImageUrls display = new ImageUrls();
        display.setEn("http://example.com/display_en.jpg");
        display.setFr("http://example.com/display_fr.jpg");
        front.setDisplay(display);
        selectedImages.setFront(front);

        // When
        product.setSelectedImages(selectedImages);

        // Then
        assertThat(product.getSelectedImages()).isEqualTo(selectedImages);
    }
}