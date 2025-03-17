package com.trinity.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceConversionTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private OpenFoodFactsService openFoodFactsService;

    @InjectMocks
    private ProductService productService;

    private String barcode;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    void setUp() {
        barcode = "1234567890123";

        // Set up ReadProductDTO
        readProductDTO = new ReadProductDTO();
        readProductDTO.setBarcode(barcode);
        readProductDTO.setName("Test Product");
        readProductDTO.setBrand("Test Brand");
        readProductDTO.setPrice(new BigDecimal("9.99"));
    }

    @Test
    void testConvertToCreateProductDTO() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Access the private method using reflection
        Method convertToCreateProductDTOMethod = ProductService.class.getDeclaredMethod("convertToCreateProductDTO", ReadProductDTO.class);
        convertToCreateProductDTOMethod.setAccessible(true);

        // Call the method
        CreateProductDTO result = (CreateProductDTO) convertToCreateProductDTOMethod.invoke(productService, readProductDTO);

        // Verify the conversion
        assertThat(result).isNotNull();
        assertThat(result.getBarcode()).isEqualTo(barcode);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getBrand()).isEqualTo("Test Brand");
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("9.99"));
    }

    @Test
    void testSetDefaultValues_NullPrice() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create DTO with null price
        CreateProductDTO createDTO = new CreateProductDTO();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(null);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductDTO.class);
        setDefaultValuesMethod.setAccessible(true);

        // Call the method
        setDefaultValuesMethod.invoke(productService, createDTO);

        // Verify default price was set
        assertThat(createDTO.getPrice()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void testSetDefaultValues_NullStock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create DTO with null stock
        CreateProductDTO createDTO = new CreateProductDTO();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(new BigDecimal("9.99"));
        createDTO.setStock(null);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductDTO.class);
        setDefaultValuesMethod.setAccessible(true);

        // Call the method
        setDefaultValuesMethod.invoke(productService, createDTO);

        // Verify default stock was set
        assertThat(createDTO.getStock()).isNotNull();
        assertThat(createDTO.getStock().getQuantity()).isEqualTo(0);
        assertThat(createDTO.getStock().getMinThreshold()).isEqualTo(5);
        assertThat(createDTO.getStock().getMaxThreshold()).isEqualTo(100);
    }

    @Test
    void testSetDefaultValues_NoChangesNeeded() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create DTO with all values set
        CreateProductDTO createDTO = new CreateProductDTO();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(new BigDecimal("9.99"));

        CreateProductDTO.StockDto stockDto = new CreateProductDTO.StockDto();
        stockDto.setQuantity(20);
        stockDto.setMinThreshold(10);
        stockDto.setMaxThreshold(200);
        createDTO.setStock(stockDto);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductDTO.class);
        setDefaultValuesMethod.setAccessible(true);

        // Call the method
        setDefaultValuesMethod.invoke(productService, createDTO);

        // Verify no values were changed
        assertThat(createDTO.getPrice()).isEqualTo(new BigDecimal("9.99"));
        assertThat(createDTO.getStock().getQuantity()).isEqualTo(20);
        assertThat(createDTO.getStock().getMinThreshold()).isEqualTo(10);
        assertThat(createDTO.getStock().getMaxThreshold()).isEqualTo(200);
    }

    @Test
    void testConvertToCreateProductDTOWithNullFields() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create ReadProductDTO with some null fields
        ReadProductDTO dto = new ReadProductDTO();
        dto.setBarcode(barcode);
        dto.setName("Test Product");
        dto.setBrand(null); // null brand
        dto.setPrice(new BigDecimal("9.99"));
        dto.setNutriscoreGrade("a");
        dto.setNutrientLevels(null); // null nutrient levels

        // Access the private method using reflection
        Method convertToCreateProductDTOMethod = ProductService.class.getDeclaredMethod("convertToCreateProductDTO", ReadProductDTO.class);
        convertToCreateProductDTOMethod.setAccessible(true);

        // Call the method
        CreateProductDTO result = (CreateProductDTO) convertToCreateProductDTOMethod.invoke(productService, dto);

        // Verify the conversion handles null fields
        assertThat(result).isNotNull();
        assertThat(result.getBarcode()).isEqualTo(barcode);
        assertThat(result.getName()).isEqualTo("Test Product");
        assertThat(result.getBrand()).isNull();
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("9.99"));
        assertThat(result.getNutriscoreGrade()).isEqualTo("a");
        assertThat(result.getNutrientLevels()).isNull();
    }
}