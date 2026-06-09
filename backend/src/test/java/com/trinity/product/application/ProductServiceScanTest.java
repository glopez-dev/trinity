package com.trinity.product.application;

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

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.domain.port.ProductRepositoryPort;
import com.trinity.product.interfaces.rest.dto.CreateProductRequest;
import com.trinity.product.interfaces.rest.dto.ProductResponse;
import com.trinity.product.exception.ProductNotFoundException;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;

@ExtendWith(MockitoExtension.class)
class ProductServiceScanTest {

    @Mock
    private ProductRepositoryPort productRepository;

    @Mock
    private ProductApiMapper productMapper;

    @Mock
    private ProductCatalogGateway productCatalogGateway;

    @InjectMocks
    private ProductService productService;

    private String barcode;
    private UUID productId;
    private Product product;
    private Product savedProduct;
    private ProductResponse readProductDTO;

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

        // Distinct instance for the post-create save result, so each toDTO stub is consumed once.
        savedProduct = Product.builder()
                .id(productId)
                .barcode(barcode)
                .name("Test Product")
                .brand("Test Brand")
                .price(new BigDecimal("9.99"))
                .build();

        ProductResponse.StockDto stockDto = new ProductResponse.StockDto();
        stockDto.setQuantity(10);
        stockDto.setMinThreshold(5);
        stockDto.setMaxThreshold(100);

        readProductDTO = new ProductResponse();
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
        ProductResponse result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productMapper).toDTO(product);
        verifyNoInteractions(productCatalogGateway);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotInDatabaseButInOpenFoodFacts_ShouldCreateAndReturnIt() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(product));
        when(productMapper.toDTO(product)).thenReturn(readProductDTO);            // catalog -> DTO at the seam
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(savedProduct);
        when(productRepository.save(savedProduct)).thenReturn(savedProduct);
        when(productMapper.toDTO(savedProduct)).thenReturn(readProductDTO);       // created -> DTO

        // WHEN
        ProductResponse result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getBarcode()).isEqualTo(barcode);
    }

    @Test
    void scanProduct_WhenProductNotFoundAnywhere_ShouldThrowException() {
        // GIVEN
        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> productService.scanProduct(barcode))
                .withMessageContaining("Product with barcode " + barcode + " not found");

        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullPrice_ShouldSetDefaultPrice() {
        // GIVEN
        Product catalogProduct = Product.builder()
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .build();

        ProductResponse openFoodFactsDTO = new ProductResponse();
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

        ProductResponse resultDTO = new ProductResponse();
        resultDTO.setId(productId);
        resultDTO.setBarcode(barcode);
        resultDTO.setName("OpenFoodFacts Product");
        resultDTO.setBrand("OpenFoodFacts Brand");
        resultDTO.setPrice(new BigDecimal("0.00"));

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(catalogProduct));
        when(productMapper.toDTO(catalogProduct)).thenReturn(openFoodFactsDTO);
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(productWithDefaultPrice);
        when(productRepository.save(productWithDefaultPrice)).thenReturn(productWithDefaultPrice);
        when(productMapper.toDTO(productWithDefaultPrice)).thenReturn(resultDTO);

        // WHEN
        ProductResponse result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getPrice()).isEqualTo(new BigDecimal("0.00"));
    }

    @Test
    void scanProduct_WhenProductFromOpenFoodFactsHasNullStock_ShouldSetDefaultStock() {
        // GIVEN
        Product catalogProduct = Product.builder()
                .barcode(barcode)
                .name("OpenFoodFacts Product")
                .brand("OpenFoodFacts Brand")
                .price(new BigDecimal("5.99"))
                .build();

        ProductResponse openFoodFactsDTO = new ProductResponse();
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

        ProductResponse resultDTO = new ProductResponse();
        resultDTO.setId(productId);
        resultDTO.setBarcode(barcode);
        resultDTO.setName("OpenFoodFacts Product");
        resultDTO.setBrand("OpenFoodFacts Brand");
        resultDTO.setPrice(new BigDecimal("5.99"));

        ProductResponse.StockDto defaultStockDto = new ProductResponse.StockDto();
        defaultStockDto.setQuantity(0);
        defaultStockDto.setMinThreshold(5);
        defaultStockDto.setMaxThreshold(100);
        resultDTO.setStock(defaultStockDto);

        when(productRepository.findByBarcode(barcode)).thenReturn(Optional.empty());
        when(productCatalogGateway.findByBarcode(barcode)).thenReturn(Optional.of(catalogProduct));
        when(productMapper.toDTO(catalogProduct)).thenReturn(openFoodFactsDTO);
        when(productMapper.toEntity(any(CreateProductRequest.class))).thenReturn(productWithDefaultStock);
        when(productRepository.save(productWithDefaultStock)).thenReturn(productWithDefaultStock);
        when(productMapper.toDTO(productWithDefaultStock)).thenReturn(resultDTO);

        // WHEN
        ProductResponse result = productService.scanProduct(barcode);

        // THEN
        verify(productRepository).findByBarcode(barcode);
        verify(productCatalogGateway).findByBarcode(barcode);
        verify(productRepository).save(any(Product.class));

        assertThat(result).isNotNull();
        assertThat(result.getStock()).isNotNull();
        assertThat(result.getStock().getQuantity()).isEqualTo(0);
        assertThat(result.getStock().getMinThreshold()).isEqualTo(5);
        assertThat(result.getStock().getMaxThreshold()).isEqualTo(100);
    }
}
