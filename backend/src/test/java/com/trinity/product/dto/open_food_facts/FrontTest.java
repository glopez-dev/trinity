package com.trinity.product.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.trinity.product.dto.ImageUrls;


class FrontTest {

    private Front front;

    @BeforeEach
    void setUp() {
        front = new Front();
    }

    @Test
    void testGetAndSetDisplay() {
        // Given
        ImageUrls display = new ImageUrls();
        display.setEn("http://example.com/display_en.jpg");
        display.setFr("http://example.com/display_fr.jpg");

        // When
        front.setDisplay(display);

        // Then
        assertThat(front.getDisplay()).isEqualTo(display);
    }

    @Test
    void testGetAndSetSmall() {
        // Given
        ImageUrls small = new ImageUrls();
        small.setEn("http://example.com/small_en.jpg");
        small.setFr("http://example.com/small_fr.jpg");

        // When
        front.setSmall(small);

        // Then
        assertThat(front.getSmall()).isEqualTo(small);
    }

    @Test
    void testGetAndSetThumb() {
        // Given
        ImageUrls thumb = new ImageUrls();
        thumb.setEn("http://example.com/thumb_en.jpg");
        thumb.setFr("http://example.com/thumb_fr.jpg");

        // When
        front.setThumb(thumb);

        // Then
        assertThat(front.getThumb()).isEqualTo(thumb);
    }
}