package com.trinity.product.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.trinity.product.dto.api.SearchProductResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutrientLevels;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutriments;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsProduct;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsSelectedImages;
import com.trinity.product.model.Product;
import com.trinity.product.model.Product.NutrientLevels;
import com.trinity.product.model.Product.Nutriments;
import com.trinity.product.model.Product.SelectedImages;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OpenFoodFactsAdapter {

    public static SearchProductResponse adapt(OpenFoodFactSearchResponse openFoodFactsResponse) {
        SearchProductResponse response = new SearchProductResponse();

        if (openFoodFactsResponse == null || openFoodFactsResponse.getProducts() == null) {
            response.setProducts(List.of());
            return response;
        }

        List<Product> products = openFoodFactsResponse.getProducts().parallelStream()
            .map(OpenFoodFactsAdapter::adaptProduct)
            .collect(Collectors.toList());

        response.setProducts(products);

        return response;
    }

    private static NutrientLevels adaptNutrientLevels(OpenFoodFactsNutrientLevels openFoodFactsNutrientLevels) {
        if (openFoodFactsNutrientLevels == null) {
            return null;
        }

        return NutrientLevels.builder()
            .saturatedFat(openFoodFactsNutrientLevels.getSaturatedFat())
            .sugars(openFoodFactsNutrientLevels.getSugars())
            .salt(openFoodFactsNutrientLevels.getSalt())
            .build();
    }

    private static Nutriments adaptNutriments(OpenFoodFactsNutriments openFoodFactsNutriments) {
        if (openFoodFactsNutriments == null) {
            return null;
        }

        return Nutriments.builder()
            .energyKcal100g(openFoodFactsNutriments.getEnergyKcal100g())
            .proteins100g(openFoodFactsNutriments.getProteins100g())
            .carbohydrates100g(openFoodFactsNutriments.getCarbohydrates100g())
            .fiber100g(openFoodFactsNutriments.getFiber100g())
            .salt100g(openFoodFactsNutriments.getSalt100g())
            .sugars100g(openFoodFactsNutriments.getSugars100g())
            .build();
    }

    private static SelectedImages adaptSelectedImages(OpenFoodFactsSelectedImages openFoodFactsSelectedImages) {
        if (openFoodFactsSelectedImages == null || openFoodFactsSelectedImages.getFront() == null) {
            return null;
        }

        return SelectedImages.builder()
            .display(openFoodFactsSelectedImages.getFront().getDisplay())
            .small(openFoodFactsSelectedImages.getFront().getSmall())
            .thumb(openFoodFactsSelectedImages.getFront().getThumb())
            .build();
    }

    private static Product adaptProduct(OpenFoodFactsProduct openFoodFactsProduct) {

        if (openFoodFactsProduct == null) {
            return null;
        }

        NutrientLevels nutrientLevels = adaptNutrientLevels(openFoodFactsProduct.getNutrientLevels());
        Nutriments nutriments = adaptNutriments(openFoodFactsProduct.getNutriments());
        SelectedImages selectedImages = adaptSelectedImages(openFoodFactsProduct.getSelectedImages());

        return Product.builder()
            .barcode(openFoodFactsProduct.getCode())
            .category(openFoodFactsProduct.getComparedToCategory())
            .brand(openFoodFactsProduct.getBrands())
            .name(openFoodFactsProduct.getGenericNameFr() != null ? openFoodFactsProduct.getGenericNameFr() : openFoodFactsProduct.getGenericNameEn())
            .ingredients(openFoodFactsProduct.getIngredientsTextFr() != null ? openFoodFactsProduct.getIngredientsTextFr() : openFoodFactsProduct.getIngredientsTextEn())
            .nutrientLevels(nutrientLevels)
            .nutriments(nutriments)
            .nutriscoreGrade(openFoodFactsProduct.getNutriscoreGrade())
            .selectedImages(selectedImages)
            .build();
    }
}
