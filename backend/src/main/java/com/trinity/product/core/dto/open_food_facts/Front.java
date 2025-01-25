package com.trinity.product.core.dto.open_food_facts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.trinity.product.core.dto.ImageUrls;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Front {
    private ImageUrls display;
    private ImageUrls small;
    private ImageUrls thumb;
}
