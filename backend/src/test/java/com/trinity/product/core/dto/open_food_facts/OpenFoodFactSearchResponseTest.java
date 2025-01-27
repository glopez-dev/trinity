package com.trinity.product.core.dto.open_food_facts;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class OpenFoodFactSearchResponseTest {

    private OpenFoodFactSearchResponse response;
    private List<OpenFoodFactsProduct> products;

    @BeforeEach
    void setUp() {
        response = new OpenFoodFactSearchResponse();
        products = new ArrayList<>();
        OpenFoodFactsProduct product = new OpenFoodFactsProduct();
        product.setCode("123456789");
        products.add(product);
    }

    @Test
    void testGetCount() {
        // Given
        int count = 10;
        response.setCount(count);

        // When
        int result = response.getCount();

        // Then
        assertThat(result).isEqualTo(count);
    }

    @Test
    void testGetPage() {
        // Given
        int page = 1;
        response.setPage(page);

        // When
        int result = response.getPage();

        // Then
        assertThat(result).isEqualTo(page);
    }

    @Test
    void testGetPageCount() {
        // Given
        int pageCount = 5;
        response.setPageCount(pageCount);

        // When
        int result = response.getPageCount();

        // Then
        assertThat(result).isEqualTo(pageCount);
    }

    @Test
    void testGetPageSize() {
        // Given
        int pageSize = 20;
        response.setPageSize(pageSize);

        // When
        int result = response.getPageSize();

        // Then
        assertThat(result).isEqualTo(pageSize);
    }

    @Test
    void testGetProducts() {
        // Given
        response.setProducts(products);

        // When
        List<OpenFoodFactsProduct> result = response.getProducts();

        // Then
        assertThat(result).isEqualTo(products);
    }
}