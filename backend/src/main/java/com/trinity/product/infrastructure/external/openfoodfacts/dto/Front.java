package com.trinity.product.infrastructure.external.openfoodfacts.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Front {
    private ImageUrls display;
    private ImageUrls small;
    private ImageUrls thumb;
}
