package com.trinity.product.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.trinity.product.dto.open_food_facts.Front;
import com.trinity.product.dto.open_food_facts.ImageUrls;
import com.trinity.product.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutrientLevels;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutriments;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsProduct;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsSelectedImages;
import com.trinity.product.model.Product;


class OpenFoodFactsAdapterTest {

    @Test
    void testAdapt_NullInput() {
        // Given
        OpenFoodFactSearchResponse openFoodFactsResponse = null;

        // When
        List<Product> response = OpenFoodFactsAdapter.adapt(openFoodFactsResponse);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    void testAdapt_EmptyProductList() {
        // Given
        OpenFoodFactSearchResponse openFoodFactsResponse = new OpenFoodFactSearchResponse();
        openFoodFactsResponse.setProducts(List.of());

        // When
        List<Product> response = OpenFoodFactsAdapter.adapt(openFoodFactsResponse);

        // Then
        assertThat(response).isEmpty();
    }

    @Test
    void testAdapt_ValidProductList() {
        // Given
        OpenFoodFactsProduct openFoodFactsProduct = new OpenFoodFactsProduct();
        openFoodFactsProduct.setCode("1234567890123");
        openFoodFactsProduct.setBrands("Coca-Cola");
        openFoodFactsProduct.setGenericNameEn("Coca-Cola Classic");
        openFoodFactsProduct.setIngredientsTextEn("Carbonated water, sugar, caramel color, phosphoric acid, natural flavors, caffeine");
        openFoodFactsProduct.setNutriscoreGrade("C");

        OpenFoodFactsNutrientLevels nutrientLevels = new OpenFoodFactsNutrientLevels();
        nutrientLevels.setSaturatedFat("Medium");
        nutrientLevels.setSugars("High");
        nutrientLevels.setSalt("Low");
        openFoodFactsProduct.setNutrientLevels(nutrientLevels);

        OpenFoodFactsNutriments nutriments = new OpenFoodFactsNutriments();
        nutriments.setEnergyKcal100g(42.0);
        nutriments.setProteins100g(0.0);
        nutriments.setCarbohydrates100g(10.6);
        nutriments.setFat100g(0.0);
        nutriments.setFiber100g(0.0);
        nutriments.setSalt100g(0.01);
        nutriments.setSugars100g(10.6);
        openFoodFactsProduct.setNutriments(nutriments);

        OpenFoodFactsSelectedImages selectedImages = new OpenFoodFactsSelectedImages();
        Front front = new Front();
        front.setDisplay(ImageUrls.builder().en("https://display-url.com").fr("https://display-url.com").build());
        front.setSmall(ImageUrls.builder().en("https://small-url.com").fr("https://small-url.com").build());
        front.setThumb(ImageUrls.builder().en("https://thumb-url.com").fr("https://thumb-url.com").build());
        selectedImages.setFront(front);
        openFoodFactsProduct.setSelectedImages(selectedImages);

        OpenFoodFactSearchResponse openFoodFactsResponse = new OpenFoodFactSearchResponse();
        openFoodFactsResponse.setProducts(List.of(openFoodFactsProduct));

        // When
        List<Product> response = OpenFoodFactsAdapter.adapt(openFoodFactsResponse);

        // Then
        assertThat(response.size()).isEqualTo(1);
        Product product = response.get(0);
        assertThat(product.getBarcode()).isEqualTo("1234567890123");
        assertThat(product.getBrand()).isEqualTo("Coca-Cola");
        assertThat(product.getName()).isEqualTo("Coca-Cola Classic");
        assertThat(product.getIngredients()).isEqualTo("Carbonated water, sugar, caramel color, phosphoric acid, natural flavors, caffeine");
        assertThat(product.getNutriscoreGrade()).isEqualTo("C");
        assertThat(product.getNutrientLevels().getSaturatedFat()).isEqualTo("Medium");
        assertThat(product.getNutrientLevels().getSugars()).isEqualTo("High");
        assertThat(product.getNutrientLevels().getSalt()).isEqualTo("Low");
        assertThat(product.getNutriments().getEnergyKcal100g()).isEqualTo(42.0);
        assertThat(product.getNutriments().getProteins100g()).isEqualTo(0.0);
        assertThat(product.getNutriments().getCarbohydrates100g()).isEqualTo(10.6);
        assertThat(product.getNutriments().getFat100g()).isEqualTo(0.0);
        assertThat(product.getNutriments().getFiber100g()).isEqualTo(0.0);
        assertThat(product.getNutriments().getSalt100g()).isEqualTo(0.01);
        assertThat(product.getNutriments().getSugars100g()).isEqualTo(10.6);
        assertThat(product.getSelectedImages().getDisplay().getEn()).isEqualTo("https://display-url.com");
        assertThat(product.getSelectedImages().getSmall().getEn()).isEqualTo("https://small-url.com");
        assertThat(product.getSelectedImages().getThumb().getEn()).isEqualTo("https://thumb-url.com");
    }
}