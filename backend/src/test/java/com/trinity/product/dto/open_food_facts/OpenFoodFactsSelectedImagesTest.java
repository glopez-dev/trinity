package com.trinity.product.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OpenFoodFactsSelectedImagesTest {

    private OpenFoodFactsSelectedImages selectedImages;

    @BeforeEach
    void setUp() {
        selectedImages = new OpenFoodFactsSelectedImages();
    }

    @Test
    void testGetAndSetFront() {
        // Given
        Front front = new Front();
        ImageUrls display = new ImageUrls();
        display.setEn("http://example.com/display_en.jpg");
        display.setFr("http://example.com/display_fr.jpg");
        front.setDisplay(display);

        // When
        selectedImages.setFront(front);

        // Then
        assertThat(selectedImages.getFront()).isEqualTo(front);
    }
}