package com.trinity.product.infrastructure.persistence.mapper;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.model.ProductImageUrl;
import com.trinity.product.infrastructure.persistence.entity.ProductEntity;
import com.trinity.product.infrastructure.persistence.entity.ProductImageUrlEmbeddable;
import org.springframework.stereotype.Component;

/**
 * Hand-written translation between the pure domain {@link Product} and its anemic
 * JPA mirror {@link ProductEntity}. Lives on the persistence side of the mapping
 * boundary: it touches only domain + entity types, never an API DTO.
 *
 * <p>Timestamp/id ownership: {@code toEntity}/{@code updateEntity} never write
 * {@code createdAt}/{@code updatedAt} so Hibernate's {@code @CreationTimestamp}/
 * {@code @UpdateTimestamp} stay authoritative; {@code lastUpdate} is copied as a
 * plain field (the column is inert/manually-settable). Every value-object
 * converter is null-propagating to preserve the "null embedded → null columns"
 * round-trip.
 */
@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product d) {
        if (d == null) {
            return null;
        }
        return ProductEntity.builder()
            .id(d.getId())                       // null on first persist -> @GeneratedValue fills it
            .barcode(d.getBarcode())
            .category(d.getCategory())
            .brand(d.getBrand())
            .name(d.getName())
            .ingredients(d.getIngredients())
            .price(d.getPrice())
            .nutrientLevels(toEntity(d.getNutrientLevels()))
            .nutriments(toEntity(d.getNutriments()))
            .nutriscoreGrade(d.getNutriscoreGrade())
            .selectedImages(toEntity(d.getSelectedImages()))
            .lastUpdate(d.getLastUpdate())       // plain copy (inert column)
            .stock(toEntity(d.getStock()))
            .build();
    }

    public Product toDomain(ProductEntity e) {
        if (e == null) {
            return null;
        }
        return Product.builder()
            .id(e.getId())
            .barcode(e.getBarcode())
            .category(e.getCategory())
            .brand(e.getBrand())
            .name(e.getName())
            .ingredients(e.getIngredients())
            .price(e.getPrice())
            .nutrientLevels(toDomain(e.getNutrientLevels()))
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .nutriments(toDomain(e.getNutriments()))
            .nutriscoreGrade(e.getNutriscoreGrade())
            .selectedImages(toDomain(e.getSelectedImages()))
            .lastUpdate(e.getLastUpdate())
            .stock(toDomain(e.getStock()))
            .build();
    }

    /** In-place sync of a managed entity (update path). Preserves id; never touches createdAt/updatedAt. */
    public void updateEntity(ProductEntity target, Product source) {
        target.setBarcode(source.getBarcode());
        target.setCategory(source.getCategory());
        target.setBrand(source.getBrand());
        target.setName(source.getName());
        target.setIngredients(source.getIngredients());
        target.setPrice(source.getPrice());
        target.setNutrientLevels(toEntity(source.getNutrientLevels()));
        target.setNutriments(toEntity(source.getNutriments()));
        target.setNutriscoreGrade(source.getNutriscoreGrade());
        target.setSelectedImages(toEntity(source.getSelectedImages()));
        target.setLastUpdate(source.getLastUpdate());
        target.setStock(toEntity(source.getStock()));
    }

    // --- null-propagating value-object converters ---

    private ProductEntity.NutrientLevels toEntity(Product.NutrientLevels d) {
        if (d == null) {
            return null;
        }
        return ProductEntity.NutrientLevels.builder()
            .fat(d.getFat()).saturatedFat(d.getSaturatedFat()).sugars(d.getSugars()).salt(d.getSalt()).build();
    }

    private Product.NutrientLevels toDomain(ProductEntity.NutrientLevels e) {
        if (e == null) {
            return null;
        }
        return Product.NutrientLevels.builder()
            .fat(e.getFat()).saturatedFat(e.getSaturatedFat()).sugars(e.getSugars()).salt(e.getSalt()).build();
    }

    private ProductEntity.Nutriments toEntity(Product.Nutriments d) {
        if (d == null) {
            return null;
        }
        return ProductEntity.Nutriments.builder()
            .energyKcal100g(d.getEnergyKcal100g()).proteins100g(d.getProteins100g())
            .carbohydrates100g(d.getCarbohydrates100g()).fat100g(d.getFat100g())
            .fiber100g(d.getFiber100g()).salt100g(d.getSalt100g()).sugars100g(d.getSugars100g()).build();
    }

    private Product.Nutriments toDomain(ProductEntity.Nutriments e) {
        if (e == null) {
            return null;
        }
        return Product.Nutriments.builder()
            .energyKcal100g(e.getEnergyKcal100g()).proteins100g(e.getProteins100g())
            .carbohydrates100g(e.getCarbohydrates100g()).fat100g(e.getFat100g())
            .fiber100g(e.getFiber100g()).salt100g(e.getSalt100g()).sugars100g(e.getSugars100g()).build();
    }

    private ProductEntity.SelectedImages toEntity(Product.SelectedImages d) {
        if (d == null) {
            return null;
        }
        return ProductEntity.SelectedImages.builder()
            .display(toEntity(d.getDisplay())).small(toEntity(d.getSmall())).thumb(toEntity(d.getThumb())).build();
    }

    private Product.SelectedImages toDomain(ProductEntity.SelectedImages e) {
        if (e == null) {
            return null;
        }
        return Product.SelectedImages.builder()
            .display(toDomain(e.getDisplay())).small(toDomain(e.getSmall())).thumb(toDomain(e.getThumb())).build();
    }

    private ProductEntity.Stock toEntity(Product.Stock d) {
        if (d == null) {
            return null;
        }
        return ProductEntity.Stock.builder()
            .quantity(d.getQuantity()).minThreshold(d.getMinThreshold()).maxThreshold(d.getMaxThreshold()).build();
    }

    private Product.Stock toDomain(ProductEntity.Stock e) {
        if (e == null) {
            return null;
        }
        return Product.Stock.builder()
            .quantity(e.getQuantity()).minThreshold(e.getMinThreshold()).maxThreshold(e.getMaxThreshold()).build();
    }

    private ProductImageUrlEmbeddable toEntity(ProductImageUrl d) {
        if (d == null) {
            return null;
        }
        return ProductImageUrlEmbeddable.builder().en(d.getEn()).fr(d.getFr()).build();
    }

    private ProductImageUrl toDomain(ProductImageUrlEmbeddable e) {
        if (e == null) {
            return null;
        }
        return ProductImageUrl.builder().en(e.getEn()).fr(e.getFr()).build();
    }
}
