package com.trinity.product.dto.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchProductRequest {

    @NotEmpty(message = "Search term must not be empty")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Search term must contain only alphabetic characters")
    private String searchTerm;
}