package com.trinity.product.core.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OpenFoodFactsNutrientLevelsTest {

    private OpenFoodFactsNutrientLevels nutrientLevels;

    @BeforeEach
    void setUp() {
        nutrientLevels = new OpenFoodFactsNutrientLevels();
    }

    @Test
    void testGetAndSetFat() {
        // Given
        String fat = "10g";

        // When
        nutrientLevels.setFat(fat);

        // Then
        assertThat(nutrientLevels.getFat()).isEqualTo(fat);
    }

    @Test
    void testGetAndSetSalt() {
        // Given
        String salt = "1g";

        // When
        nutrientLevels.setSalt(salt);

        // Then
        assertThat(nutrientLevels.getSalt()).isEqualTo(salt);
    }

    @Test
    void testGetAndSetSaturatedFat() {
        // Given
        String saturatedFat = "5g";

        // When
        nutrientLevels.setSaturatedFat(saturatedFat);

        // Then
        assertThat(nutrientLevels.getSaturatedFat()).isEqualTo(saturatedFat);
    }

    @Test
    void testGetAndSetSugars() {
        // Given
        String sugars = "15g";

        // When
        nutrientLevels.setSugars(sugars);

        // Then
        assertThat(nutrientLevels.getSugars()).isEqualTo(sugars);
    }
}