package com.trinity.product.core.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsNutrientLevels {
    @JsonProperty("fat")
    private String fat;

    @JsonProperty("salt")
    private String salt;

    @JsonProperty("saturated-fat")
    private String saturatedFat;
    
    @JsonProperty("sugars")
    private String sugars;
}