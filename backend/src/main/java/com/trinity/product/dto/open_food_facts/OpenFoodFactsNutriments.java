package com.trinity.product.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsNutriments {
    
    @JsonProperty("energy-kcal")
    private double energyKcal;
    @JsonProperty("energy-kcal_100g")
    private double energyKcal100g;

    private double proteins;
    @JsonProperty("proteins_100g")
    private double proteins100g;

    private double carbohydrates;
    @JsonProperty("carbohydrates_100g")
    private double carbohydrates100g;

    private double fat;
    @JsonProperty("fat_100g")
    private double fat100g;

    private double fiber;
    @JsonProperty("fiber_100g")
    private double fiber100g;

    private double salt;
    @JsonProperty("salt_100g")
    private double salt100g;

    private double sugars;
    @JsonProperty("sugars_100g")
    private double sugars100g;
}
   