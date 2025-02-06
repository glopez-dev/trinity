package com.trinity.product.dto.open_food_facts;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class OpenFoodFactsNutrimentsTest {

    @Test
    void testEnergyKcal() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedEnergyKcal = 100.0;

        // When
        nutriments.setEnergyKcal(expectedEnergyKcal);

        // Then
        assertEquals(expectedEnergyKcal, nutriments.getEnergyKcal());
    }

    @Test
    void testEnergyKcal100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedEnergyKcal100g = 50.0;

        // When
        nutriments.setEnergyKcal100g(expectedEnergyKcal100g);

        // Then
        assertEquals(expectedEnergyKcal100g, nutriments.getEnergyKcal100g());
    }

    @Test
    void testProteins() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedProteins = 10.0;

        // When
        nutriments.setProteins(expectedProteins);

        // Then
        assertEquals(expectedProteins, nutriments.getProteins());
    }

    @Test
    void testProteins100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedProteins100g = 5.0;

        // When
        nutriments.setProteins100g(expectedProteins100g);

        // Then
        assertEquals(expectedProteins100g, nutriments.getProteins100g());
    }

    @Test
    void testCarbohydrates() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedCarbohydrates = 20.0;

        // When
        nutriments.setCarbohydrates(expectedCarbohydrates);

        // Then
        assertEquals(expectedCarbohydrates, nutriments.getCarbohydrates());
    }

    @Test
    void testCarbohydrates100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedCarbohydrates100g = 10.0;

        // When
        nutriments.setCarbohydrates100g(expectedCarbohydrates100g);

        // Then
        assertEquals(expectedCarbohydrates100g, nutriments.getCarbohydrates100g());
    }

    @Test
    void testFat() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedFat = 15.0;

        // When
        nutriments.setFat(expectedFat);

        // Then
        assertEquals(expectedFat, nutriments.getFat());
    }

    @Test
    void testFat100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedFat100g = 7.5;

        // When
        nutriments.setFat100g(expectedFat100g);

        // Then
        assertEquals(expectedFat100g, nutriments.getFat100g());
    }

    @Test
    void testFiber() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedFiber = 5.0;

        // When
        nutriments.setFiber(expectedFiber);

        // Then
        assertEquals(expectedFiber, nutriments.getFiber());
    }

    @Test
    void testFiber100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedFiber100g = 2.5;

        // When
        nutriments.setFiber100g(expectedFiber100g);

        // Then
        assertEquals(expectedFiber100g, nutriments.getFiber100g());
    }

    @Test
    void testSalt() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedSalt = 1.0;

        // When
        nutriments.setSalt(expectedSalt);

        // Then
        assertEquals(expectedSalt, nutriments.getSalt());
    }

    @Test
    void testSalt100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedSalt100g = 0.5;

        // When
        nutriments.setSalt100g(expectedSalt100g);

        // Then
        assertEquals(expectedSalt100g, nutriments.getSalt100g());
    }

    @Test
    void testSugars() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedSugars = 30.0;

        // When
        nutriments.setSugars(expectedSugars);

        // Then
        assertEquals(expectedSugars, nutriments.getSugars());
    }

    @Test
    void testSugars100g() {
        // Given
        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        double expectedSugars100g = 15.0;

        // When
        nutriments.setSugars100g(expectedSugars100g);

        // Then
        assertEquals(expectedSugars100g, nutriments.getSugars100g());
    }
}