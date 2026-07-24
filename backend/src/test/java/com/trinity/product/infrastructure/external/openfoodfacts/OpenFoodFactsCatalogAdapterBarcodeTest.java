package com.trinity.product.infrastructure.external.openfoodfacts;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClient;

import com.trinity.product.domain.model.Product;
import com.trinity.product.infrastructure.external.openfoodfacts.dto.Front;
import com.trinity.product.infrastructure.external.openfoodfacts.dto.OpenFoodFactSearchResponse;
import com.trinity.product.infrastructure.external.openfoodfacts.dto.OpenFoodFactsProduct;
import com.trinity.product.infrastructure.external.openfoodfacts.dto.OpenFoodFactsSelectedImages;

import reactor.core.publisher.Mono;

/**
 * Exercises the barcode-lookup path of the OpenFoodFacts gateway adapter. Mocks
 * only the WebClient chain (the static adapt() coupling is gone — adapt is now an
 * instance method on the adapter), and asserts the legacy null-on-error / empty
 * behaviour, now surfaced as {@link Optional#empty()}. Lenient strictness is set
 * explicitly so the per-test WebClient-chain stubs do not trip strict stubbing.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class OpenFoodFactsCatalogAdapterBarcodeTest {

    @Mock
    private WebClient webClient;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    private OpenFoodFactsCatalogAdapter adapter;

    private String barcode;
    private OpenFoodFactSearchResponse openFoodFactsResponse;

    @BeforeEach
    void setUp() {
        adapter = new OpenFoodFactsCatalogAdapter(webClient);

        barcode = "3017620422003";

        OpenFoodFactsProduct openFoodFactsProduct = new OpenFoodFactsProduct();
        openFoodFactsProduct.setCode(barcode);
        openFoodFactsProduct.setBrands("Ferrero");
        openFoodFactsProduct.setGenericNameFr("Nutella");

        openFoodFactsResponse = new OpenFoodFactSearchResponse();
        openFoodFactsResponse.setProducts(Collections.singletonList(openFoodFactsProduct));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ValidBarcode_ReturnsProduct() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(openFoodFactsResponse));

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then — adapter returns domain directly, no mapper hop
        assertTrue(result.isPresent());
        assertEquals(barcode, result.get().getBarcode());
        assertEquals("Ferrero", result.get().getBrand());
        assertEquals("Nutella", result.get().getName());

        // Verify URI
        ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        String capturedUri = uriCaptor.getValue().toString();
        assertTrue(capturedUri.contains(barcode));
        assertTrue(capturedUri.contains("world.openfoodfacts.org/api/v0/product/"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ProductNotFound_ReturnsEmpty() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        OpenFoodFactSearchResponse emptyResponse = new OpenFoodFactSearchResponse();
        emptyResponse.setProducts(Collections.emptyList());
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(emptyResponse));

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_ApiError_ReturnsEmpty() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class))
                .thenReturn(Mono.error(new RuntimeException("API Error")));

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_NullResponse_ReturnsEmpty() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.empty());

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void getProductByBarcode_NullProductElement_ReturnsEmpty() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // A non-empty product list whose single element is null -> adaptProduct yields null.
        OpenFoodFactSearchResponse nullElementResponse = new OpenFoodFactSearchResponse();
        nullElementResponse.setProducts(Collections.singletonList(null));
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(nullElementResponse));

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void search_ValidTerm_ReturnsAdaptedProducts() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class))
                .thenReturn(Mono.just(openFoodFactsResponse));

        // When
        List<Product> result = adapter.search("nutella");

        // Then
        assertEquals(1, result.size());
        assertEquals(barcode, result.get(0).getBarcode());
        assertEquals("Ferrero", result.get(0).getBrand());

        // The search URI must target the cgi/search endpoint and carry the term.
        ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        String capturedUri = uriCaptor.getValue().toString();
        assertTrue(capturedUri.contains("world.openfoodfacts.org/cgi/search.pl"));
        assertTrue(capturedUri.contains("search_terms=nutella"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void search_NullResponse_ReturnsEmptyList() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.empty());

        // When
        List<Product> result = adapter.search("nutella");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void buildUri_ContainsAllQueryParams() {
        URI uri = adapter.buildUri("coca");
        String s = uri.toString();

        assertTrue(s.contains("world.openfoodfacts.org/cgi/search.pl"));
        assertTrue(s.contains("search_terms=coca"));
        assertTrue(s.contains("page_size=10"));
        assertTrue(s.contains("json=1"));
        assertTrue(s.contains("sort_by=unique_scans_n"));
        assertTrue(s.contains("fields="));
    }

    @SuppressWarnings("unchecked")
    @Test
    void findByBarcode_ProductWithNullImageUrls_MapsNullImages() {
        // A product carrying a SelectedImages.front whose display/small/thumb are all null:
        // adaptImageUrl(null) must yield null rather than throwing.
        OpenFoodFactsProduct productWithImages = new OpenFoodFactsProduct();
        productWithImages.setCode(barcode);
        productWithImages.setBrands("Ferrero");
        productWithImages.setGenericNameFr("Nutella");
        OpenFoodFactsSelectedImages selectedImages = new OpenFoodFactsSelectedImages();
        selectedImages.setFront(new Front());
        productWithImages.setSelectedImages(selectedImages);

        OpenFoodFactSearchResponse response = new OpenFoodFactSearchResponse();
        response.setProducts(Collections.singletonList(productWithImages));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(OpenFoodFactSearchResponse.class)).thenReturn(Mono.just(response));

        // When
        Optional<Product> result = adapter.findByBarcode(barcode);

        // Then
        assertTrue(result.isPresent());
        assertNotNull(result.get().getSelectedImages());
        assertNull(result.get().getSelectedImages().getDisplay());
    }
}
