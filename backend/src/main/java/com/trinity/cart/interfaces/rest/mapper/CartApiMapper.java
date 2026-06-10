package com.trinity.cart.interfaces.rest.mapper;

import com.trinity.cart.domain.model.Cart;
import com.trinity.cart.domain.model.CartItem;
import com.trinity.cart.interfaces.rest.dto.CartItemRequest;
import com.trinity.cart.interfaces.rest.dto.CartItemResponse;
import com.trinity.cart.interfaces.rest.dto.CartResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps the Cart aggregate to its API representation. The Money value object is
 * flattened to (totalAmount, currency), matching the JSON shape clients already
 * consume.
 */
@Mapper(componentModel = "spring")
public interface CartApiMapper {

    @Mapping(target = "totalAmount", source = "totalAmount.amount")
    @Mapping(target = "currency", source = "totalAmount.currency")
    CartResponse toResponse(Cart cart);

    CartItemResponse toItemResponse(CartItem item);

    /** Transitional: the domain item is still built from the client payload until prices are resolved server-side. */
    CartItem toDomain(CartItemRequest request);
}
