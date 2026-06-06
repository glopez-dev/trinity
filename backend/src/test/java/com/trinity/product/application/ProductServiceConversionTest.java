package com.trinity.product.application;

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

import com.trinity.product.dto.api.CreateProductRequest;
import com.trinity.product.dto.api.ProductResponse;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;

@ExtendWith(MockitoExtension.class)
class ProductServiceConversionTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ProductApiMapper productMapper;

    @Mock
    private ProductCatalogGateway productCatalogGateway;

    @InjectMocks
    private ProductService productService;

    private String barcode;
    private ProductResponse readProductDTO;

    @BeforeEach
    void setUp() {
        barcode = "1234567890123";

        // Set up ProductResponse
        readProductDTO = new ProductResponse();
        readProductDTO.setBarcode(barcode);
        readProductDTO.setName("Test Product");
        readProductDTO.setBrand("Test Brand");
        readProductDTO.setPrice(new BigDecimal("9.99"));
    }

    @Test
    void testConvertToCreateProductRequest() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Access the private method using reflection
        Method convertToCreateProductRequestMethod = ProductService.class.getDeclaredMethod("convertToCreateProductRequest", ProductResponse.class);
        convertToCreateProductRequestMethod.setAccessible(true);

        // Call the method
        CreateProductRequest result = (CreateProductRequest) convertToCreateProductRequestMethod.invoke(productService, readProductDTO);

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
        CreateProductRequest createDTO = new CreateProductRequest();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(null);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductRequest.class);
        setDefaultValuesMethod.setAccessible(true);

        // Call the method
        setDefaultValuesMethod.invoke(productService, createDTO);

        // Verify default price was set
        assertThat(createDTO.getPrice()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void testSetDefaultValues_NullStock() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create DTO with null stock
        CreateProductRequest createDTO = new CreateProductRequest();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(new BigDecimal("9.99"));
        createDTO.setStock(null);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductRequest.class);
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
        CreateProductRequest createDTO = new CreateProductRequest();
        createDTO.setBarcode(barcode);
        createDTO.setName("Test Product");
        createDTO.setBrand("Test Brand");
        createDTO.setPrice(new BigDecimal("9.99"));

        CreateProductRequest.StockDto stockDto = new CreateProductRequest.StockDto();
        stockDto.setQuantity(20);
        stockDto.setMinThreshold(10);
        stockDto.setMaxThreshold(200);
        createDTO.setStock(stockDto);

        // Access private method using reflection
        Method setDefaultValuesMethod = ProductService.class.getDeclaredMethod("setDefaultValues", CreateProductRequest.class);
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
    void testConvertToCreateProductRequestWithNullFields() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        // Create ProductResponse with some null fields
        ProductResponse dto = new ProductResponse();
        dto.setBarcode(barcode);
        dto.setName("Test Product");
        dto.setBrand(null); // null brand
        dto.setPrice(new BigDecimal("9.99"));
        dto.setNutriscoreGrade("a");
        dto.setNutrientLevels(null); // null nutrient levels

        // Access the private method using reflection
        Method convertToCreateProductRequestMethod = ProductService.class.getDeclaredMethod("convertToCreateProductRequest", ProductResponse.class);
        convertToCreateProductRequestMethod.setAccessible(true);

        // Call the method
        CreateProductRequest result = (CreateProductRequest) convertToCreateProductRequestMethod.invoke(productService, dto);

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