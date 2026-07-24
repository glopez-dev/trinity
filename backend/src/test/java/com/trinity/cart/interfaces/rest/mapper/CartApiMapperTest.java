package com.trinity.cart.interfaces.rest.mapper;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.cart.interfaces.rest.dto.CartResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CartApiMapperTest {

    private final CartApiMapper mapper = Mappers.getMapper(CartApiMapper.class);

    @Test
    void toResponse_flattensMoneyIntoAmountAndCurrency() {
        UUID customerId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = Cart.builder().customerId(customerId).build();
        cart.addItem(CartItem.builder()
                .productId(productId)
                .productName("Apples")
                .quantity(3)
                .unitPrice(new BigDecimal("2.50"))
                .build());

        CartResponse response = mapper.toResponse(cart);

        assertThat(response.customerId()).isEqualTo(customerId);
        assertThat(response.totalAmount()).isEqualByComparingTo("7.50");
        assertThat(response.currency()).isEqualTo("USD");
        assertThat(response.items()).hasSize(1);
        var item = response.items().iterator().next();
        assertThat(item.productId()).isEqualTo(productId);
        assertThat(item.productName()).isEqualTo("Apples");
        assertThat(item.totalPrice()).isEqualByComparingTo("7.50");
    }

}
