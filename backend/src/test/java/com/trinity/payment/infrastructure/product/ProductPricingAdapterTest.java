package com.trinity.payment.infrastructure.product;

import com.trinity.payment.domain.port.ProductPricingPort;
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
class ProductPricingAdapterTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductPricingAdapter adapter;

    @Test
    void findById_mapsTheProductToThePaymentView() {
        UUID productId = UUID.randomUUID();
        when(productService.getProduct(productId)).thenReturn(Product.builder()
                .id(productId)
                .name("Coffee")
                .price(new BigDecimal("10.00"))
                .build());

        Optional<ProductPricingPort.PricedProduct> priced = adapter.findById(productId);

        assertThat(priced).isPresent();
        assertThat(priced.get().name()).isEqualTo("Coffee");
        assertThat(priced.get().unitPrice()).isEqualByComparingTo("10.00");
    }

    @Test
    void findById_unknownProduct_returnsEmpty() {
        UUID productId = UUID.randomUUID();
        when(productService.getProduct(productId)).thenThrow(new ProductNotFoundException("missing"));

        assertThat(adapter.findById(productId)).isEmpty();
    }
}
