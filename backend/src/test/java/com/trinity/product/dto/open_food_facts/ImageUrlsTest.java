package com.trinity.product.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ImageUrlsTest {

    private ImageUrls imageUrls;

    @BeforeEach
    void setUp() {
        imageUrls = new ImageUrls();
    }

    @Test
    void testGetAndSetEn() {
        // Given
        String enUrl = "http://example.com/en.jpg";

        // When
        imageUrls.setEn(enUrl);

        // Then
        assertThat(imageUrls.getEn()).isEqualTo(enUrl);
    }

    @Test
    void testGetAndSetFr() {
        // Given
        String frUrl = "http://example.com/fr.jpg";

        // When
        imageUrls.setFr(frUrl);

        // Then
        assertThat(imageUrls.getFr()).isEqualTo(frUrl);
    }
}