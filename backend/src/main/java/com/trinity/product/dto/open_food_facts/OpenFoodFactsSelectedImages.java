package com.trinity.product.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactsSelectedImages {
    private Front front;
}
