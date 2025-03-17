package com.trinity.product.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.exception.ProductNotFoundException;
import com.trinity.product.mapper.ProductMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;

@ExtendWith(MockitoExtension.class)
class ProductServiceScanTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private OpenFoodFactsService openFoodFactsService;

    @InjectMocks
    private ProductService productService;

    private String barcode;
    private UUID productId;
    private Product product;
    private ReadProductDTO readProductDTO;

    @BeforeEach
    void setUp() {
        barcode = "1234567890123";
        productId = UUID.randomUUID();

        Product.Stock stock = Product.Stock.builder()
                .quantity(10)
                .minThreshold(5)
                .maxThreshold(100)
                .build();

        product = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("Test Product")
                .brand("Test Brand")
                .price(new BigDecimal("9.99"))
                .stock(stock)
                .build();

        ReadProductDTO.StockDto stockDto = new ReadProductDTO.StockDto();
        stockDto.setQuantity(10);
        stockDto.setMinThreshold(5);
        stockDto.setMaxThreshold(100);

        readProductDTO = new ReadProductDTO();
        readProductDTO.setId(productId);
        readProductDTO.setBarcode(barcode);
        readProductDTO.setName("Test Product");
        readProductDTO.setBrand("Test Brand");
        readProductDTO.setPrice(new BigDecimal("9.99"));
        readProductDTO.setStock(stockDto);
    }

    @Test
    void scanProduct_WhenProductFoundInDatabase_ShouldReturnIt() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(readProductDTO);

        // WHEN
        ReadProductDTO result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productMapper).toDTO(product);
        verifyNoInteractions(openFoodFactsService);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotInDatabaseButInOpenFoodFacts_ShouldCreateAndReturnIt() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(openFoodFactsService.getProductByBarcode(barcode)).thenReturn(readProductDTO);
        when(productMapper.toEntity(any(CreateProductDTO.class))).thenReturn(product);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDTO(product)).thenReturn(readProductDTO);

        // WHEN
        ReadProductDTO result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(openFoodFactsService).getProductByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotFoundAnywhere_ShouldThrowException() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(openFoodFactsService.getProductByBarcode(barcode)).thenReturn(null);

        // WHEN & THEN
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.scanProduct(barcode))
                .withMessageContaining("Product with barcode " + barcode + " not found");

        verify(productRepository).findByBarcode(barcode);
        verify(openFoodFactsService).getProductByBarcode(barcode);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullPrice_ShouldSetDefaultPrice() {
        // GIVEN
        ReadProductDTO openFoodFactsDTO = new ReadProductDTO();
        openFoodFactsDTO.setBarcode(barcode);
        openFoodFactsDTO.setName("OpenFoodFacts Product");
        openFoodFactsDTO.setBrand("OpenFoodFacts Brand");
        openFoodFactsDTO.setPrice(null);

        Product productWithDefaultPrice = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .price(new BigDecimal("0.00"))
                .build();

        ReadProductDTO resultDTO = new ReadProductDTO();
        resultDTO.setId(productId);
        resultDTO.setBarcode(barcode);
        resultDTO.setName("OpenFoodFacts Product");
        resultDTO.setBrand("OpenFoodFacts Brand");
        resultDTO.setPrice(new BigDecimal("0.00"));

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(openFoodFactsService.getProductByBarcode(barcode)).thenReturn(openFoodFactsDTO);
        when(productMapper.toEntity(any(CreateProductDTO.class))).thenReturn(productWithDefaultPrice);
        when(productRepository.save(productWithDefaultPrice)).thenReturn(productWithDefaultPrice);
        when(productMapper.toDTO(productWithDefaultPrice)).thenReturn(resultDTO);

        // WHEN
        ReadProductDTO result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(openFoodFactsService).getProductByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullStock_ShouldSetDefaultStock() {
        // GIVEN
        ReadProductDTO openFoodFactsDTO = new ReadProductDTO();
        openFoodFactsDTO.setBarcode(barcode);
        openFoodFactsDTO.setName("OpenFoodFacts Product");
        openFoodFactsDTO.setBrand("OpenFoodFacts Brand");
        openFoodFactsDTO.setPrice(new BigDecimal("5.99"));
        openFoodFactsDTO.setStock(null);

        Product.Stock defaultStock = Product.Stock.builder()
                .quantity(0)
                .minThreshold(5)
                .maxThreshold(100)
                .build();

        Product productWithDefaultStock = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .price(new BigDecimal("5.99"))
                .stock(defaultStock)
                .build();

        ReadProductDTO resultDTO = new ReadProductDTO();
        resultDTO.setId(productId);
        resultDTO.setBarcode(barcode);
        resultDTO.setName("OpenFoodFacts Product");
        resultDTO.setBrand("OpenFoodFacts Brand");
        resultDTO.setPrice(new BigDecimal("5.99"));

        ReadProductDTO.StockDto defaultStockDto = new ReadProductDTO.StockDto();
        defaultStockDto.setQuantity(0);
        defaultStockDto.setMinThreshold(5);
        defaultStockDto.setMaxThreshold(100);
        resultDTO.setStock(defaultStockDto);

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(openFoodFactsService.getProductByBarcode(barcode)).thenReturn(openFoodFactsDTO);
        when(productMapper.toEntity(any(CreateProductDTO.class))).thenReturn(productWithDefaultStock);
        when(productRepository.save(productWithDefaultStock)).thenReturn(productWithDefaultStock);
        when(productMapper.toDTO(productWithDefaultStock)).thenReturn(resultDTO);

        // WHEN
        ReadProductDTO result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(openFoodFactsService).getProductByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getStock()).isNotNull();
        assertThat(result.getStock().getQuantity()).isEqualTo(0);
        assertThat(result.getStock().getMinThreshold()).isEqualTo(5);
        assertThat(result.getStock().getMaxThreshold()).isEqualTo(100);
    }
}