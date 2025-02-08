package com.trinity.product.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Front {
    private ImageUrls display;
    private ImageUrls small;
    private ImageUrls thumb;
}
