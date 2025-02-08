package com.trinity.product.mapper;

import org.springframework.stereotype.Service;

import com.trinity.product.dto.ImageUrls;
import com.trinity.product.dto.api.CreateProductDTO;
import com.trinity.product.dto.api.ReadProductDTO;
import com.trinity.product.model.Product;
import com.trinity.product.model.Product.NutrientLevels;

@Service
public class ProductMapper {

    public Product toEntity(CreateProductDTO productDTO) {
        Product productEntity = new Product();

        productEntity.setBarcode(productDTO.getBarcode());
        productEntity.setCategory(productDTO.getCategory());
        productEntity.setBrand(productDTO.getBrand());
        productEntity.setName(productDTO.getName());
        productEntity.setIngredients(productDTO.getIngredients());
        productEntity.setPrice(productDTO.getPrice());

        if (productDTO.getStock() != null) {
            productEntity.setStock(Product.Stock.builder()
                .quantity(productDTO.getStock().getQuantity())
                .minThreshold(productDTO.getStock().getMinThreshold())
                .maxThreshold(productDTO.getStock().getMaxThreshold())
                .build()
            );
        }

        if (productDTO.getNutrientLevels() != null) {
            productEntity.setNutrientLevels(NutrientLevels.builder()
                .fat(productDTO.getNutrientLevels().getFat())
                .saturatedFat(productDTO.getNutrientLevels().getSaturatedFat())
                .sugars(productDTO.getNutrientLevels().getSugars())
                .salt(productDTO.getNutrientLevels().getSalt())
                .build()
            );
        }

        if (productDTO.getNutriments() != null) {
            productEntity.setNutriments(Product.Nutriments.builder()
                .energyKcal100g(productDTO.getNutriments().getEnergyKcal100g())
                .proteins100g(productDTO.getNutriments().getProteins100g())
                .carbohydrates100g(productDTO.getNutriments().getCarbohydrates100g())
                .fat100g(productDTO.getNutriments().getFat100g())
                .fiber100g(productDTO.getNutriments().getFiber100g())
                .salt100g(productDTO.getNutriments().getSalt100g())
                .sugars100g(productDTO.getNutriments().getSugars100g())
                .build()
            );
        }

        productEntity.setNutriscoreGrade(productDTO.getNutriscoreGrade());

        if (productDTO.getSelectedImages() != null) {
            productEntity.setSelectedImages(Product.SelectedImages.builder()
                .display(ImageUrls.builder()
                    .en(productDTO.getSelectedImages().getDisplay().getEn())
                    .fr(productDTO.getSelectedImages().getDisplay().getFr())
                    .build()
                )
                .small(ImageUrls.builder()
                    .en(productDTO.getSelectedImages().getSmall().getEn())
                    .fr(productDTO.getSelectedImages().getSmall().getFr())
                    .build()
                )
                .thumb(ImageUrls.builder()
                    .en(productDTO.getSelectedImages().getThumb().getEn())
                    .fr(productDTO.getSelectedImages().getThumb().getFr())
                    .build()
                )
                .build()
            );
        }

        return productEntity;
    }

        public ReadProductDTO toDTO(Product mappedProduct) {
            if (mappedProduct == null) {
                return null;
            }
            ReadProductDTO dto = new ReadProductDTO();
 
            dto.setId(mappedProduct.getId());
            dto.setBarcode(mappedProduct.getBarcode());
            dto.setCategory(mappedProduct.getCategory());
            dto.setBrand(mappedProduct.getBrand());
            dto.setName(mappedProduct.getName());
            dto.setIngredients(mappedProduct.getIngredients());
            dto.setPrice(mappedProduct.getPrice());
            dto.setNutriscoreGrade(mappedProduct.getNutriscoreGrade());

            if (mappedProduct.getNutrientLevels() != null) {
                ReadProductDTO.NutrientLevelsDto nlDto = new ReadProductDTO.NutrientLevelsDto();
                nlDto.setFat(mappedProduct.getNutrientLevels().getFat());
                nlDto.setSaturatedFat(mappedProduct.getNutrientLevels().getSaturatedFat());
                nlDto.setSugars(mappedProduct.getNutrientLevels().getSugars());
                nlDto.setSalt(mappedProduct.getNutrientLevels().getSalt());
                dto.setNutrientLevels(nlDto);
            }

            if (mappedProduct.getNutriments() != null) {
                ReadProductDTO.NutrimentsDto nmDto = new ReadProductDTO.NutrimentsDto();
                nmDto.setEnergyKcal100g(mappedProduct.getNutriments().getEnergyKcal100g());
                nmDto.setProteins100g(mappedProduct.getNutriments().getProteins100g());
                nmDto.setCarbohydrates100g(mappedProduct.getNutriments().getCarbohydrates100g());
                nmDto.setFat100g(mappedProduct.getNutriments().getFat100g());
                nmDto.setFiber100g(mappedProduct.getNutriments().getFiber100g());
                nmDto.setSalt100g(mappedProduct.getNutriments().getSalt100g());
                nmDto.setSugars100g(mappedProduct.getNutriments().getSugars100g());
                dto.setNutriments(nmDto);
            }

            if (mappedProduct.getSelectedImages() != null) {
                ReadProductDTO.SelectedImagesDto siDto = new ReadProductDTO.SelectedImagesDto();
                if (mappedProduct.getSelectedImages().getDisplay() != null) {
                    ReadProductDTO.SelectedImagesDto.DisplayImagesDto dispDto = 
                        new ReadProductDTO.SelectedImagesDto.DisplayImagesDto();
                    dispDto.setEn(mappedProduct.getSelectedImages().getDisplay().getEn());
                    dispDto.setFr(mappedProduct.getSelectedImages().getDisplay().getFr());
                    siDto.setDisplay(dispDto);
                }
                if (mappedProduct.getSelectedImages().getSmall() != null) {
                    ReadProductDTO.SelectedImagesDto.DisplayImagesDto smallDto = 
                        new ReadProductDTO.SelectedImagesDto.DisplayImagesDto();
                    smallDto.setEn(mappedProduct.getSelectedImages().getSmall().getEn());
                    smallDto.setFr(mappedProduct.getSelectedImages().getSmall().getFr());
                    siDto.setSmall(smallDto);
                }
                if (mappedProduct.getSelectedImages().getThumb() != null) {
                    ReadProductDTO.SelectedImagesDto.DisplayImagesDto thumbDto = 
                        new ReadProductDTO.SelectedImagesDto.DisplayImagesDto();
                    thumbDto.setEn(mappedProduct.getSelectedImages().getThumb().getEn());
                    thumbDto.setFr(mappedProduct.getSelectedImages().getThumb().getFr());
                    siDto.setThumb(thumbDto);
                }
                dto.setSelectedImages(siDto);
            }

            if (mappedProduct.getStock() != null) {
                ReadProductDTO.StockDto stockDto = new ReadProductDTO.StockDto();
                stockDto.setQuantity(mappedProduct.getStock().getQuantity());
                stockDto.setMinThreshold(mappedProduct.getStock().getMinThreshold());
                stockDto.setMaxThreshold(mappedProduct.getStock().getMaxThreshold());
                dto.setStock(stockDto);
            }

            return dto;
        }
}
