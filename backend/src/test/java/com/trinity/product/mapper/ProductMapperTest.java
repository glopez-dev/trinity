package com.trinity.product.mapper;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.trinity.product.dto.ImageUrls;
import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.dto.api.CreateProductDTO.NutrientLevelsDto;
import com.trinity.product.dto.api.CreateProductDTO.NutrimentsDto;
import com.trinity.product.dto.api.CreateProductDTO.SelectedImagesDto;
import com.trinity.product.dto.api.CreateProductDTO.StockDto;
import com.trinity.product.dto.api.CreateProductDTO.SelectedImagesDto.DisplayImagesDto;
import com.trinity.product.model.Product;
import java.math.BigDecimal;


class ProductMapperTest {

    private ProductMapper productMapper;

    @BeforeEach
    void setUp() {
        productMapper = new ProductMapper();
    }

    @Test
    void testToEntity_AllFields() {
        // GIVEN
        CreateProductDTO dto = new CreateProductDTO();
        dto.setBarcode("123456");
        dto.setCategory("Beverages");
        dto.setBrand("Test Brand");
        dto.setName("Test Product");
        dto.setIngredients("Water, Sugar");
        dto.setPrice(BigDecimal.valueOf(9.99));
        dto.setNutriscoreGrade("c");

        NutrientLevelsDto nlDto = new NutrientLevelsDto();
        nlDto.setFat("low");
        nlDto.setSaturatedFat("moderate");
        nlDto.setSugars("high");
        nlDto.setSalt("low");
        dto.setNutrientLevels(nlDto);

        NutrimentsDto nmDto = new NutrimentsDto();
        nmDto.setEnergyKcal100g(100.0);
        nmDto.setProteins100g(2.0);
        nmDto.setCarbohydrates100g(20.0);
        nmDto.setFat100g(0.5);
        nmDto.setFiber100g(1.5);
        nmDto.setSalt100g(0.8);
        nmDto.setSugars100g(10.0);
        dto.setNutriments(nmDto);

        SelectedImagesDto siDto = new SelectedImagesDto();
        DisplayImagesDto disp = new DisplayImagesDto();
        disp.setEn("display_en.jpg");
        disp.setFr("display_fr.jpg");
        siDto.setDisplay(disp);
        DisplayImagesDto small = new DisplayImagesDto();
        small.setEn("small_en.jpg");
        small.setFr("small_fr.jpg");
        siDto.setSmall(small);
        DisplayImagesDto thumb = new DisplayImagesDto();
        thumb.setEn("thumb_en.jpg");
        thumb.setFr("thumb_fr.jpg");
        siDto.setThumb(thumb);
        dto.setSelectedImages(siDto);

        StockDto stockDto = new StockDto();
        stockDto.setQuantity(100);
        stockDto.setMinThreshold(10);
        stockDto.setMaxThreshold(200);
        dto.setStock(stockDto);

        // WHEN
        Product entity = productMapper.toEntity(dto);

        // THEN
        assertNotNull(entity);
        assertEquals("123456", entity.getBarcode());
        assertEquals("Beverages", entity.getCategory());
        assertEquals("Test Brand", entity.getBrand());
        assertEquals("Test Product", entity.getName());
        assertEquals("Water, Sugar", entity.getIngredients());
        assertEquals(BigDecimal.valueOf(9.99), entity.getPrice());
        assertEquals("c", entity.getNutriscoreGrade());
        assertNotNull(entity.getNutrientLevels());
        assertEquals("low", entity.getNutrientLevels().getFat());
        assertEquals("moderate", entity.getNutrientLevels().getSaturatedFat());
        assertEquals("high", entity.getNutrientLevels().getSugars());
        assertEquals("low", entity.getNutrientLevels().getSalt());
        assertNotNull(entity.getNutriments());
        assertEquals(100.0, entity.getNutriments().getEnergyKcal100g());
        assertEquals(2.0, entity.getNutriments().getProteins100g());
        assertEquals(20.0, entity.getNutriments().getCarbohydrates100g());
        assertEquals(0.5, entity.getNutriments().getFat100g());
        assertEquals(1.5, entity.getNutriments().getFiber100g());
        assertEquals(0.8, entity.getNutriments().getSalt100g());
        assertEquals(10.0, entity.getNutriments().getSugars100g());
        assertNotNull(entity.getSelectedImages());
        assertNotNull(entity.getSelectedImages().getDisplay());
        assertEquals("display_en.jpg", entity.getSelectedImages().getDisplay().getEn());
        assertEquals("display_fr.jpg", entity.getSelectedImages().getDisplay().getFr());
        assertEquals(100, entity.getStock().getQuantity());
        assertEquals(10, entity.getStock().getMinThreshold());
        assertEquals(200, entity.getStock().getMaxThreshold());
    }

    @Test
    void testToEntity_NullSubObjects() {
        // GIVEN
        CreateProductDTO dto = new CreateProductDTO();
        dto.setBarcode("654321");
        dto.setPrice(BigDecimal.TEN);

        // WHEN
        Product entity = productMapper.toEntity(dto);

        // THEN
        assertNotNull(entity);
        assertEquals("654321", entity.getBarcode());
        assertNull(entity.getCategory());
        assertNull(entity.getBrand());
        assertNull(entity.getName());
        assertNull(entity.getIngredients());
        assertEquals(BigDecimal.TEN, entity.getPrice());
        assertNull(entity.getNutrientLevels());
        assertNull(entity.getNutriments());
        assertNull(entity.getSelectedImages());
        assertNull(entity.getStock());
    }

    @Test
    void testToDTO_AllFields() {
        // GIVEN
        Product product = new Product();
        product.setBarcode("999999");
        product.setCategory("Snacks");
        product.setBrand("BrandTest");
        product.setName("Chips");
        product.setIngredients("Potato, Salt");
        product.setPrice(BigDecimal.valueOf(1.99));
        product.setNutriscoreGrade("b");

        Product.NutrientLevels nl = new Product.NutrientLevels();
        nl.setFat("high");
        nl.setSaturatedFat("high");
        nl.setSugars("low");
        nl.setSalt("moderate");
        product.setNutrientLevels(nl);

        Product.Nutriments nm = new Product.Nutriments();
        nm.setEnergyKcal100g(500.0);
        nm.setProteins100g(4.0);
        nm.setCarbohydrates100g(70.0);
        nm.setFat100g(20.0);
        nm.setFiber100g(5.0);
        nm.setSalt100g(1.0);
        nm.setSugars100g(1.5);
        product.setNutriments(nm);

        Product.SelectedImages si = new Product.SelectedImages();
        si.setDisplay(ImageUrls.builder().en("bigen.jpg").fr("bigfr.jpg").build());
        si.setSmall(ImageUrls.builder().en("smallen.jpg").fr("smallfr.jpg").build());
        si.setThumb(ImageUrls.builder().en("thumben.jpg").fr("thumbfr.jpg").build());
        product.setSelectedImages(si);

        Product.Stock stock = new Product.Stock();
        stock.setQuantity(50);
        stock.setMinThreshold(5);
        stock.setMaxThreshold(100);
        product.setStock(stock);

        // WHEN
        ReadProductDTO dto = productMapper.toDTO(product);

        // THEN
        assertNotNull(dto);
        assertEquals("999999", dto.getBarcode());
        assertEquals("Snacks", dto.getCategory());
        assertEquals("BrandTest", dto.getBrand());
        assertEquals("Chips", dto.getName());
        assertEquals("Potato, Salt", dto.getIngredients());
        assertEquals(BigDecimal.valueOf(1.99), dto.getPrice());
        assertEquals("b", dto.getNutriscoreGrade());
        assertNotNull(dto.getNutrientLevels());
        assertEquals("high", dto.getNutrientLevels().getFat());
        assertEquals("high", dto.getNutrientLevels().getSaturatedFat());
        assertEquals("low", dto.getNutrientLevels().getSugars());
        assertEquals("moderate", dto.getNutrientLevels().getSalt());
        assertNotNull(dto.getNutriments());
        assertEquals(500.0, dto.getNutriments().getEnergyKcal100g());
        assertEquals(4.0, dto.getNutriments().getProteins100g());
        assertEquals(70.0, dto.getNutriments().getCarbohydrates100g());
        assertEquals(20.0, dto.getNutriments().getFat100g());
        assertEquals(5.0, dto.getNutriments().getFiber100g());
        assertEquals(1.0, dto.getNutriments().getSalt100g());
        assertEquals(1.5, dto.getNutriments().getSugars100g());
        assertNotNull(dto.getSelectedImages());
        assertNotNull(dto.getSelectedImages().getDisplay());
        assertEquals("bigen.jpg", dto.getSelectedImages().getDisplay().getEn());
        assertEquals("bigfr.jpg", dto.getSelectedImages().getDisplay().getFr());
        assertNotNull(dto.getStock());
        assertEquals(50, dto.getStock().getQuantity());
        assertEquals(5, dto.getStock().getMinThreshold());
        assertEquals(100, dto.getStock().getMaxThreshold());
    }

    @Test
    void testToDTO_NullProduct() {
        // GIVEN
        Product product = null;

        // WHEN
        ReadProductDTO dto = productMapper.toDTO(product);

        // THEN
        assertNull(dto);
    }
}