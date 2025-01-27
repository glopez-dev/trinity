package com.trinity.product.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageUrls {
    @Column(insertable = false, updatable = false)
    private String en;
    @Column(insertable = false, updatable = false)
    private String fr;
}