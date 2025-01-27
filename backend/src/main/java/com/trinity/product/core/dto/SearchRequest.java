package com.trinity.product.core.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Search term must contain only alphabetic characters")
    private String searchTerm;
}