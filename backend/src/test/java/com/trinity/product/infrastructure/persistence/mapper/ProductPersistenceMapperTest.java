package com.trinity.product.infrastructure.persistence.mapper;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.model.ProductImageUrl;
import com.trinity.product.infrastructure.persistence.entity.ProductEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Round-trip coverage for the hand-written domain<->entity mapper. A single
 * dropped sub-field here would be a silent data loss that ddl-validate cannot
 * catch, so the full aggregate graph (5 value objects, 6 image fields, 3
 * timestamps) is exercised both ways plus the update-in-place path.
 */
class ProductPersistenceMapperTest {

    private final ProductPersistenceMapper mapper = new ProductPersistenceMapper();

    private Product fullDomainProduct() {
        return Product.builder()
            .id(UUID.randomUUID())
            .barcode("3017620422003")
            .category("Spreads")
            .brand("Ferrero")
            .name("Nutella")
            .ingredients("Sugar, Palm Oil, Hazelnuts")
            .price(new BigDecimal("4.99"))
            .nutriscoreGrade("d")
            .nutrientLevels(Product.NutrientLevels.builder()
                .fat("high").saturatedFat("high").sugars("high").salt("low").build())
            .nutriments(Product.Nutriments.builder()
                .energyKcal100g(539.0).proteins100g(6.3).carbohydrates100g(57.5)
                .fat100g(30.9).fiber100g(0.0).salt100g(0.107).sugars100g(56.3).build())
            .selectedImages(Product.SelectedImages.builder()
                .display(ProductImageUrl.builder().en("display-en.jpg").fr("display-fr.jpg").build())
                .small(ProductImageUrl.builder().en("small-en.jpg").fr("small-fr.jpg").build())
                .thumb(ProductImageUrl.builder().en("thumb-en.jpg").fr("thumb-fr.jpg").build())
                .build())
            .stock(Product.Stock.builder().quantity(42).minThreshold(5).maxThreshold(100).build())
            .build();
    }

    @Test
    void toEntity_then_toDomain_roundtrips_allFields() {
        Product source = fullDomainProduct();

        Product result = mapper.toDomain(mapper.toEntity(source));

        assertThat(result.getId()).isEqualTo(source.getId());
        assertThat(result.getBarcode()).isEqualTo("3017620422003");
        assertThat(result.getCategory()).isEqualTo("Spreads");
        assertThat(result.getBrand()).isEqualTo("Ferrero");
        assertThat(result.getName()).isEqualTo("Nutella");
        assertThat(result.getIngredients()).isEqualTo("Sugar, Palm Oil, Hazelnuts");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("4.99"));
        assertThat(result.getNutriscoreGrade()).isEqualTo("d");

        assertThat(result.getNutrientLevels().getFat()).isEqualTo("high");
        assertThat(result.getNutrientLevels().getSaturatedFat()).isEqualTo("high");
        assertThat(result.getNutrientLevels().getSugars()).isEqualTo("high");
        assertThat(result.getNutrientLevels().getSalt()).isEqualTo("low");

        assertThat(result.getNutriments().getEnergyKcal100g()).isEqualTo(539.0);
        assertThat(result.getNutriments().getProteins100g()).isEqualTo(6.3);
        assertThat(result.getNutriments().getCarbohydrates100g()).isEqualTo(57.5);
        assertThat(result.getNutriments().getFat100g()).isEqualTo(30.9);
        assertThat(result.getNutriments().getFiber100g()).isEqualTo(0.0);
        assertThat(result.getNutriments().getSalt100g()).isEqualTo(0.107);
        assertThat(result.getNutriments().getSugars100g()).isEqualTo(56.3);

        assertThat(result.getSelectedImages().getDisplay().getEn()).isEqualTo("display-en.jpg");
        assertThat(result.getSelectedImages().getDisplay().getFr()).isEqualTo("display-fr.jpg");
        assertThat(result.getSelectedImages().getSmall().getEn()).isEqualTo("small-en.jpg");
        assertThat(result.getSelectedImages().getSmall().getFr()).isEqualTo("small-fr.jpg");
        assertThat(result.getSelectedImages().getThumb().getEn()).isEqualTo("thumb-en.jpg");
        assertThat(result.getSelectedImages().getThumb().getFr()).isEqualTo("thumb-fr.jpg");

        assertThat(result.getStock().getQuantity()).isEqualTo(42);
        assertThat(result.getStock().getMinThreshold()).isEqualTo(5);
        assertThat(result.getStock().getMaxThreshold()).isEqualTo(100);
    }

    @Test
    void toEntity_nullVOs_leaveNulls() {
        Product source = Product.builder()
            .barcode("000").name("Bare").price(new BigDecimal("1.00"))
            .build();

        ProductEntity entity = mapper.toEntity(source);

        assertThat(entity.getNutrientLevels()).isNull();
        assertThat(entity.getNutriments()).isNull();
        assertThat(entity.getSelectedImages()).isNull();
        assertThat(entity.getStock()).isNull();

        Product back = mapper.toDomain(entity);
        assertThat(back.getNutrientLevels()).isNull();
        assertThat(back.getNutriments()).isNull();
        assertThat(back.getSelectedImages()).isNull();
        assertThat(back.getStock()).isNull();
    }

    @Test
    void zeroNutrimentsPreserved() {
        Product source = Product.builder()
            .barcode("111").name("Zeroes").price(new BigDecimal("1.00"))
            .nutriments(Product.Nutriments.builder().build()) // all primitives default 0.0
            .build();

        Product result = mapper.toDomain(mapper.toEntity(source));

        assertThat(result.getNutriments()).isNotNull();
        assertThat(result.getNutriments().getFat100g()).isEqualTo(0.0);
        assertThat(result.getNutriments().getEnergyKcal100g()).isEqualTo(0.0);
    }

    @Test
    void toDomain_copiesIdAndTimestamps() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(2);
        LocalDateTime updated = LocalDateTime.now().minusDays(1);
        LocalDateTime last = LocalDateTime.now();

        ProductEntity entity = ProductEntity.builder()
            .id(id).barcode("222").name("Stamped").price(new BigDecimal("2.00"))
            .createdAt(created).updatedAt(updated).lastUpdate(last)
            .build();

        Product domain = mapper.toDomain(entity);

        assertThat(domain.getId()).isEqualTo(id);
        assertThat(domain.getCreatedAt()).isEqualTo(created);
        assertThat(domain.getUpdatedAt()).isEqualTo(updated);
        assertThat(domain.getLastUpdate()).isEqualTo(last);
    }

    @Test
    void updateEntity_preservesScalars_andLastUpdate() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now().minusDays(3);
        ProductEntity managed = ProductEntity.builder()
            .id(id).barcode("333").name("Old").price(new BigDecimal("3.00")).createdAt(created)
            .build();

        LocalDateTime newLast = LocalDateTime.now();
        Product source = Product.builder()
            .barcode("333").name("New").price(new BigDecimal("9.00")).lastUpdate(newLast)
            .stock(Product.Stock.builder().quantity(7).minThreshold(1).maxThreshold(20).build())
            .build();

        mapper.updateEntity(managed, source);

        // id + createdAt untouched (timestamps owned by Hibernate / identity preserved)
        assertThat(managed.getId()).isEqualTo(id);
        assertThat(managed.getCreatedAt()).isEqualTo(created);
        // scalars + VOs synced from source
        assertThat(managed.getName()).isEqualTo("New");
        assertThat(managed.getPrice()).isEqualTo(new BigDecimal("9.00"));
        assertThat(managed.getLastUpdate()).isEqualTo(newLast);
        assertThat(managed.getStock().getQuantity()).isEqualTo(7);
    }
}
