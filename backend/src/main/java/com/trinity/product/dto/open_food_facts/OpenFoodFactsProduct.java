package com.trinity.product.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
/**
 * Represents a product from the Open Food Facts database.
 * This class contains various properties of the product such as allergens, brands, code, 
 * generic names, ingredients, nutrient levels, nutriments, nutriscore grade, and selected images.
 */
public class OpenFoodFactsProduct {

    private String allergens;

    private String brands;

    private String code;

    private String comparedToCategory;

    @JsonProperty("generic_name_fr")
    private String genericNameFr;

    @JsonProperty("generic_name_en")
    private String genericNameEn;

    @JsonProperty("ingredients_text_fr")
    private String ingredientsTextFr;

    @JsonProperty("ingredients_text_en")
    private String ingredientsTextEn;

    @JsonProperty("nutrient_levels")
    private OpenFoodFactsNutrientLevels nutrientLevels;

    private OpenFoodFactsNutriments nutriments;

    @JsonProperty("nutriscore_grade")
    private String nutriscoreGrade;

    @JsonProperty("selected_images")
    private OpenFoodFactsSelectedImages selectedImages;
}



 
