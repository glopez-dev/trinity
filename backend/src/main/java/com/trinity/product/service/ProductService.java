package com.trinity.product.service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinity.product.exception.ProductNotFoundException;
import com.trinity.product.exception.InvalidProductDataException;
import com.trinity.product.dto.api.*;
import com.trinity.product.interfaces.rest.mapper.ProductApiMapper;
import com.trinity.product.model.Product;
import com.trinity.product.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final ProductApiMapper productMapper;
    private final OpenFoodFactsService openFoodFactsService;

    @Transactional
    public ReadProductDTO createProduct(CreateProductDTO productDTO) {
        validateProductData(productDTO);
        Product mappedProduct = productMapper.toEntity(productDTO);
        Product savedProduct = productRepository.save(mappedProduct);
        logger.info("Created new product with ID: {}", savedProduct.getId());
        return productMapper.toDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ReadProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        logger.debug("Retrieved {} products", products.size());
        return products.isEmpty() ?
            Collections.emptyList() :
            products.stream().map(productMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public ReadProductDTO getProduct(UUID productId) {
        Product product = findProductById(productId);
        return productMapper.toDTO(product);
    }

    @Transactional
    public ReadProductDTO updateProduct(UUID productId, UpdateProductDTO request) {
        Product product = findProductById(productId);

        request.getName().ifPresent(product::setName);
        request.getPrice().ifPresent(product::changePrice);
        request.getQuantity().ifPresent(product::adjustStock);

        Product updatedProduct = productRepository.save(product);
        logger.info("Updated product with ID: {}", productId);
        return productMapper.toDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID productId) {
        Product product = findProductById(productId);
        productRepository.delete(product);
        logger.info("Deleted product with ID: {}", productId);
    }

    private Product findProductById(UUID productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("Product not found with ID: %s", productId)));
    }

    private void validateProductData(CreateProductDTO productDTO) {
        if (productDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidProductDataException("Price cannot be negative");
        }
        if (productDTO.getStock().getQuantity() < 0) {
            throw new InvalidProductDataException("Initial stock quantity cannot be negative");
        }
    }

    @Transactional
    public ReadProductDTO scanProduct(String barcode) {
        Optional<Product> existingProduct = productRepository.findByBarcode(barcode);

        if (existingProduct.isPresent()) {
            logger.info("Product with barcode {} found in database", barcode);
            return productMapper.toDTO(existingProduct.get());
        }

        logger.info("Product with barcode {} not found in database, checking OpenFoodFacts", barcode);
        ReadProductDTO openFoodFactsProduct = openFoodFactsService.getProductByBarcode(barcode);

        if (openFoodFactsProduct != null) {
            CreateProductDTO createProductDTO = convertToCreateProductDTO(openFoodFactsProduct);
            setDefaultValues(createProductDTO);
            return createProduct(createProductDTO);
        }

        throw new ProductNotFoundException(String.format("Product with barcode %s not found", barcode));
    }

    private CreateProductDTO convertToCreateProductDTO(ReadProductDTO readProductDTO) {
        CreateProductDTO createDTO = new CreateProductDTO();
        createDTO.setBarcode(readProductDTO.getBarcode());
        createDTO.setCategory(readProductDTO.getCategory());
        createDTO.setBrand(readProductDTO.getBrand());
        createDTO.setName(readProductDTO.getName());
        createDTO.setIngredients(readProductDTO.getIngredients());
        createDTO.setPrice(readProductDTO.getPrice());

        if (readProductDTO.getNutrientLevels() != null) {
            CreateProductDTO.NutrientLevelsDto nutrientLevels = new CreateProductDTO.NutrientLevelsDto();
            nutrientLevels.setFat(readProductDTO.getNutrientLevels().getFat());
            nutrientLevels.setSaturatedFat(readProductDTO.getNutrientLevels().getSaturatedFat());
            nutrientLevels.setSugars(readProductDTO.getNutrientLevels().getSugars());
            nutrientLevels.setSalt(readProductDTO.getNutrientLevels().getSalt());
            createDTO.setNutrientLevels(nutrientLevels);
        }

        if (readProductDTO.getNutriments() != null) {
            CreateProductDTO.NutrimentsDto nutriments = new CreateProductDTO.NutrimentsDto();
            nutriments.setEnergyKcal100g(readProductDTO.getNutriments().getEnergyKcal100g());
            nutriments.setProteins100g(readProductDTO.getNutriments().getProteins100g());
            nutriments.setCarbohydrates100g(readProductDTO.getNutriments().getCarbohydrates100g());
            nutriments.setFat100g(readProductDTO.getNutriments().getFat100g());
            nutriments.setFiber100g(readProductDTO.getNutriments().getFiber100g());
            nutriments.setSalt100g(readProductDTO.getNutriments().getSalt100g());
            nutriments.setSugars100g(readProductDTO.getNutriments().getSugars100g());
            createDTO.setNutriments(nutriments);
        }

        createDTO.setNutriscoreGrade(readProductDTO.getNutriscoreGrade());

        if (readProductDTO.getSelectedImages() != null) {
            CreateProductDTO.SelectedImagesDto selectedImages = getSelectedImagesDto(readProductDTO);

            createDTO.setSelectedImages(selectedImages);
        }

        return createDTO;
    }

    private static CreateProductDTO.SelectedImagesDto getSelectedImagesDto(ReadProductDTO readProductDTO) {
        CreateProductDTO.SelectedImagesDto selectedImages = new CreateProductDTO.SelectedImagesDto();

        if (readProductDTO.getSelectedImages().getDisplay() != null) {
            CreateProductDTO.SelectedImagesDto.DisplayImagesDto display =
                    new CreateProductDTO.SelectedImagesDto.DisplayImagesDto();
            display.setEn(readProductDTO.getSelectedImages().getDisplay().getEn());
            display.setFr(readProductDTO.getSelectedImages().getDisplay().getFr());
            selectedImages.setDisplay(display);
        }

        if (readProductDTO.getSelectedImages().getSmall() != null) {
            CreateProductDTO.SelectedImagesDto.DisplayImagesDto small =
                    new CreateProductDTO.SelectedImagesDto.DisplayImagesDto();
            small.setEn(readProductDTO.getSelectedImages().getSmall().getEn());
            small.setFr(readProductDTO.getSelectedImages().getSmall().getFr());
            selectedImages.setSmall(small);
        }

        if (readProductDTO.getSelectedImages().getThumb() != null) {
            CreateProductDTO.SelectedImagesDto.DisplayImagesDto thumb =
                    new CreateProductDTO.SelectedImagesDto.DisplayImagesDto();
            thumb.setEn(readProductDTO.getSelectedImages().getThumb().getEn());
            thumb.setFr(readProductDTO.getSelectedImages().getThumb().getFr());
            selectedImages.setThumb(thumb);
        }
        return selectedImages;
    }

    private void setDefaultValues(CreateProductDTO productDTO) {
        if (productDTO.getPrice() == null) {
            productDTO.setPrice(new BigDecimal("0.00"));
        }

        if (productDTO.getStock() == null) {
            CreateProductDTO.StockDto stockDto = new CreateProductDTO.StockDto();
            stockDto.setQuantity(0);
            stockDto.setMinThreshold(5);
            stockDto.setMaxThreshold(100);
            productDTO.setStock(stockDto);
        }
    }
}