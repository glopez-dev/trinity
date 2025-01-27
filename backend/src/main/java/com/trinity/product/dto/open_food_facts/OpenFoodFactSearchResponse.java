package com.trinity.product.dto.open_food_facts;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenFoodFactSearchResponse {
    private int count;
    private int page;
    private int pageCount;
    private int pageSize;
    private List<OpenFoodFactsProduct> products;
}
