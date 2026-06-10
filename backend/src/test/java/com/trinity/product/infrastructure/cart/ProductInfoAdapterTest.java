package com.trinity.product.infrastructure.cart;

import com.trinity.cart.domain.port.ProductInfoPort;
import com.trinity.product.application.ProductService;
import com.trinity.product.domain.exception.ProductNotFoundException;
import com.trinity.product.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductInfoAdapterTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductInfoAdapter adapter;

    @Test
    void findById_mapsTheProductToTheCartView() {
        UUID productId = UUID.randomUUID();
        when(productService.getProduct(productId)).thenReturn(Product.builder()
                .id(productId)
                .name("Apples")
                .price(new BigDecimal("2.50"))
                .build());

        Optional<ProductInfoPort.ProductInfo> info = adapter.findById(productId);

        assertThat(info).isPresent();
        assertThat(info.get().name()).isEqualTo("Apples");
        assertThat(info.get().unitPrice()).isEqualByComparingTo("2.50");
    }

    @Test
    void findById_unknownProduct_returnsEmpty() {
        UUID productId = UUID.randomUUID();
        when(productService.getProduct(productId)).thenThrow(new ProductNotFoundException("missing"));

        assertThat(adapter.findById(productId)).isEmpty();
    }
}
