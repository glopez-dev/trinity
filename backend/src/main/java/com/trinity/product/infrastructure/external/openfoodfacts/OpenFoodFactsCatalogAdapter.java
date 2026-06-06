package com.trinity.product.infrastructure.external.openfoodfacts;

import com.trinity.product.domain.model.Product;
import com.trinity.product.domain.model.ProductImageUrl;
import com.trinity.product.domain.port.ProductCatalogGateway;
import com.trinity.product.dto.open_food_facts.ImageUrls;
import com.trinity.product.dto.open_food_facts.OpenFoodFactSearchResponse;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutrientLevels;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsNutriments;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsProduct;
import com.trinity.product.dto.open_food_facts.OpenFoodFactsSelectedImages;
import com.trinity.product.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Anti-corruption adapter to OpenFoodFacts — the only place the WebClient and the
 * OpenFoodFacts wire DTOs are allowed. Translates the external representation into
 * domain {@link Product}. Behaviour is reproduced verbatim from the former
 * OpenFoodFactsService + OpenFoodFactsAdapter (including the legacy quirks: some
 * nutrient fields are intentionally left unmapped, no price/stock on import, and
 * null-on-error barcode lookups).
 */
@Component
@RequiredArgsConstructor
public class OpenFoodFactsCatalogAdapter implements ProductCatalogGateway {

    static final String FIELDS_TO_GET = "products,allergens_imported,allergens,code,brands,brand_imported,compared_to_category,grade,ingredients_text_fr,nutrient_levels,nutriments,product_name_fr_imported,quantity_imported,selected_images,nutriscore_grade,generic_name_fr,generic_name_en,ingredients_text_en";

    private static final Logger logger = LoggerFactory.getLogger(OpenFoodFactsCatalogAdapter.class);

    private final WebClient webClient;

    @Override
    public List<Product> search(String searchTerm) {
        OpenFoodFactSearchResponse response = getSearchResponseJson(buildUri(searchTerm));
        return adapt(response);
    }

    @Override
    public Optional<Product> findByBarcode(String barcode) {
        URI uri = UriComponentsBuilder
                .fromHttpUrl("https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json")
                .build()
                .toUri();
        try {
            OpenFoodFactSearchResponse response = webClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(OpenFoodFactSearchResponse.class)
                    .onErrorMap(error -> new ApiException("OpenFoodFacts API error", error))
                    .block();

            if (response != null && response.getProducts() != null && !response.getProducts().isEmpty()) {
                return Optional.ofNullable(adapt(response).get(0));
            }
            return Optional.empty();
        } catch (Exception e) {
            logger.error("Error fetching product by barcode {}: {}", barcode, e.getMessage());
            return Optional.empty();
        }
    }

    URI buildUri(String searchTerm) {
        return UriComponentsBuilder.fromHttpUrl("https://world.openfoodfacts.org/cgi/search.pl")
                .queryParam("search_terms", searchTerm)
                .queryParam("fields", FIELDS_TO_GET)
                .queryParam("page_size", 10)
                .queryParam("json", 1)
                .queryParam("sort_by", "unique_scans_n")
                .build()
                .toUri();
    }

    OpenFoodFactSearchResponse getSearchResponseJson(URI uri) {
        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(OpenFoodFactSearchResponse.class)
                .onErrorMap(error -> new ApiException("OpenFoodFacts API error", error))
                .block();
    }

    // --- OpenFoodFacts wire DTO -> domain (quirks preserved verbatim) ---

    List<Product> adapt(OpenFoodFactSearchResponse response) {
        if (response == null || response.getProducts() == null) {
            return List.of();
        }
        return response.getProducts().parallelStream()
                .map(this::adaptProduct)
                .collect(Collectors.toList());
    }

    private Product.NutrientLevels adaptNutrientLevels(OpenFoodFactsNutrientLevels x) {
        if (x == null) {
            return null;
        }
        // Legacy quirk: fat is intentionally not mapped.
        return Product.NutrientLevels.builder()
                .saturatedFat(x.getSaturatedFat())
                .sugars(x.getSugars())
                .salt(x.getSalt())
                .build();
    }

    private Product.Nutriments adaptNutriments(OpenFoodFactsNutriments x) {
        if (x == null) {
            return null;
        }
        // Legacy quirk: fat100g is intentionally not mapped (stays 0.0).
        return Product.Nutriments.builder()
                .energyKcal100g(x.getEnergyKcal100g())
                .proteins100g(x.getProteins100g())
                .carbohydrates100g(x.getCarbohydrates100g())
                .fiber100g(x.getFiber100g())
                .salt100g(x.getSalt100g())
                .sugars100g(x.getSugars100g())
                .build();
    }

    private Product.SelectedImages adaptSelectedImages(OpenFoodFactsSelectedImages x) {
        if (x == null || x.getFront() == null) {
            return null;
        }
        return Product.SelectedImages.builder()
                .display(adaptImageUrl(x.getFront().getDisplay()))
                .small(adaptImageUrl(x.getFront().getSmall()))
                .thumb(adaptImageUrl(x.getFront().getThumb()))
                .build();
    }

    private ProductImageUrl adaptImageUrl(ImageUrls x) {
        if (x == null) {
            return null;
        }
        return ProductImageUrl.builder().en(x.getEn()).fr(x.getFr()).build();
    }

    private Product adaptProduct(OpenFoodFactsProduct p) {
        if (p == null) {
            return null;
        }
        // Legacy quirk: no price, no stock on import (defaulted later by the application service).
        return Product.builder()
                .barcode(p.getCode())
                .category(p.getComparedToCategory())
                .brand(p.getBrands())
                .name(p.getGenericNameFr() != null ? p.getGenericNameFr() : p.getGenericNameEn())
                .ingredients(p.getIngredientsTextFr() != null ? p.getIngredientsTextFr() : p.getIngredientsTextEn())
                .nutrientLevels(adaptNutrientLevels(p.getNutrientLevels()))
                .nutriments(adaptNutriments(p.getNutriments()))
                .nutriscoreGrade(p.getNutriscoreGrade())
                .selectedImages(adaptSelectedImages(p.getSelectedImages()))
                .build();
    }
}
