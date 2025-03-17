// package com.trinity.payment.stripe.service;

// import com.trinity.payment.stripe.dto.ProductRequest;
// import com.trinity.payment.stripe.dto.StripeResponse;
// import com.stripe.exception.StripeException;
// import com.stripe.model.checkout.Session;
// import com.stripe.param.checkout.SessionCreateParams;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.MockedStatic;
// import org.mockito.MockitoAnnotations;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.ArrayList;
// import java.util.List;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// class StripeServiceTest {

//     @InjectMocks
//     private StripeService stripeService;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//         stripeService = new StripeService();
//     }

//     @Test
//     void checkoutProducts_Success() throws StripeException {
//         // Arrange
//         List<ProductRequest> productRequests = createProductRequests();
//         Session mockSession = mock(Session.class);
        
//         try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
//             // Setup the mock before using it
//             when(mockSession.getId()).thenReturn("test_session_id");
//             when(mockSession.getUrl()).thenReturn("https://test.checkout.url");
            
//             // Now use the mock in the static mock setup
//             mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);
            
//             // Act
//             StripeResponse response = stripeService.checkoutProducts(productRequests);
            
//             // Assert
//             assertEquals("SUCCESS", response.getStatus());
//             assertEquals("Payment session created", response.getMessage());
//             assertEquals("test_session_id", response.getSessionId());
//             assertEquals("https://test.checkout.url", response.getSessionUrl());
            
//             // Verify the session was created
//             mockedSession.verify(() -> Session.create(any(SessionCreateParams.class)));
//         }
//     }

//     @Test
//     void checkoutProducts_Exception() throws StripeException {
//         // Arrange
//         List<ProductRequest> productRequests = createProductRequests();
//         StripeException stripeException = new com.stripe.exception.InvalidRequestException("Test exception", null, null, null, null, null);
        
//         try (MockedStatic<Session> mockedSession = mockStatic(Session.class)) {
//             // Setup the mock to throw an exception
//             mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenThrow(stripeException);
            
//             // Act
//             StripeResponse response = stripeService.checkoutProducts(productRequests);
            
//             // Assert
//             assertEquals("FAILED", response.getStatus());
//             assertEquals("Error creating payment session", response.getMessage());
//             assertNull(response.getSessionId());
//             assertNull(response.getSessionUrl());
//         }
//     }

//     @Test
//     void buildLineItem_WithCustomCurrency() throws Exception {
//         // Arrange
//         ProductRequest productRequest = new ProductRequest();
//         productRequest.setName("Test Product");
//         productRequest.setAmount(1000L);
//         productRequest.setQuantity(2L);
//         productRequest.setCurrency("EUR");
        
//         // Use reflection to access private method
//         java.lang.reflect.Method buildLineItemMethod = StripeService.class.getDeclaredMethod(
//                 "buildLineItem", ProductRequest.class);
//         buildLineItemMethod.setAccessible(true);
        
//         // Act
//         SessionCreateParams.LineItem lineItem = 
//                 (SessionCreateParams.LineItem) buildLineItemMethod.invoke(stripeService, productRequest);
        
//         // Assert
//         assertEquals(2L, lineItem.getQuantity());
//         assertEquals("EUR", lineItem.getPriceData().getCurrency());
//         assertEquals(1000L, lineItem.getPriceData().getUnitAmount());
//         assertEquals("Test Product", lineItem.getPriceData().getProductData().getName());
//     }

//     @Test
//     void buildLineItem_WithDefaultCurrency() throws Exception {
//         // Arrange
//         ProductRequest productRequest = new ProductRequest();
//         productRequest.setName("Test Product");
//         productRequest.setAmount(1000L);
//         productRequest.setQuantity(2L);
//         productRequest.setCurrency(null);
        
//         // Use reflection to access private method
//         java.lang.reflect.Method buildLineItemMethod = StripeService.class.getDeclaredMethod(
//                 "buildLineItem", ProductRequest.class);
//         buildLineItemMethod.setAccessible(true);
        
//         // Act
//         SessionCreateParams.LineItem lineItem = 
//                 (SessionCreateParams.LineItem) buildLineItemMethod.invoke(stripeService, productRequest);
        
//         // Assert
//         assertEquals(2L, lineItem.getQuantity());
//         assertEquals("USD", lineItem.getPriceData().getCurrency());
//         assertEquals(1000L, lineItem.getPriceData().getUnitAmount());
//         assertEquals("Test Product", lineItem.getPriceData().getProductData().getName());
//     }

//     private List<ProductRequest> createProductRequests() {
//         List<ProductRequest> productRequests = new ArrayList<>();
        
//         ProductRequest product1 = new ProductRequest();
//         product1.setName("Product 1");
//         product1.setAmount(1000L);
//         product1.setQuantity(1L);
//         product1.setCurrency("USD");
//         productRequests.add(product1);
        
//         ProductRequest product2 = new ProductRequest();
//         product2.setName("Product 2");
//         product2.setAmount(2000L);
//         product2.setQuantity(2L);
//         product2.setCurrency("USD");
//         productRequests.add(product2);
        
//         return productRequests;
//     }
// }