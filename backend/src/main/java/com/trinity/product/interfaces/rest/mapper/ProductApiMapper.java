package com.trinity.product.interfaces.rest.mapper;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.model.Product;
import com.trinity.product.model.ProductImageUrl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Maps between the Product aggregate and its API DTOs. MapStruct resolves the
 * nested value objects (NutrientLevels, Nutriments, Stock, SelectedImages) by
 * matching property names; explicit methods below cover the image VO whose DTO
 * counterpart is named differently.
 */
@Mapper(componentModel = "spring")
public interface ProductApiMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lastUpdate", ignore = true)
    Product toEntity(CreateProductDTO dto);

    ReadProductDTO toDTO(Product product);

    // --- nested image VO <-> DTO ---

    ProductImageUrl toImageUrl(CreateProductDTO.SelectedImagesDto.DisplayImagesDto dto);

    ReadProductDTO.SelectedImagesDto.DisplayImagesDto toDisplayImagesDto(ProductImageUrl imageUrl);
}
