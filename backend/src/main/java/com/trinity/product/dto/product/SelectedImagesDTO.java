package com.trinity.product.dto.product;

import lombok.Data;

@Data
public class SelectedImagesDTO {
    private DisplayImagesDTO display;
    private DisplayImagesDTO small;
    private DisplayImagesDTO thumb;

    @Data
    public static class DisplayImagesDTO {
        private String en;
        private String fr;
    }
}