package com.trinity.payment.paypal.service;

import com.trinity.payment.paypal.adapter.PaypalInvoiceAdapter;
import com.trinity.payment.paypal.config.PaypalConfig;
import com.trinity.payment.paypal.dto.InvoiceDTO;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaypalInvoiceServiceTest {

    @Mock
    private PaypalConfig paypalConfig;

    @Mock
    private PaypalInvoiceAdapter invoiceAdapter;

    @Mock
    private APIContext apiContext;

    @InjectMocks
    private PaypalInvoiceService paypalInvoiceService;

    private Invoice mockInvoice;
    private InvoiceDTO mockInvoiceDTO;
    private static final String INVOICE_ID = "INV2-QXWN-W3VH-Q8H7-XH8J";
    private static MockedStatic<Invoice> invoiceMockedStatic;

    @BeforeAll
    static void setUpClass() {
        invoiceMockedStatic = mockStatic(Invoice.class);
    }

    @AfterAll
    static void tearDownClass() {
        invoiceMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        mockInvoice = mock(Invoice.class);
        mockInvoiceDTO = new InvoiceDTO();
        when(paypalConfig.getAPIContext()).thenReturn(apiContext);
        invoiceMockedStatic.when(() -> Invoice.get(any(APIContext.class), eq(INVOICE_ID))).thenReturn(mockInvoice);
    }

    @Test
    void createInvoice_Success() throws PayPalRESTException {
        // Arrange
        when(invoiceAdapter.mapToInvoice(any(InvoiceDTO.class))).thenReturn(mockInvoice);
        when(invoiceAdapter.mapToInvoiceDTO(any(Invoice.class))).thenReturn(mockInvoiceDTO);
        when(mockInvoice.create(any(APIContext.class))).thenReturn(mockInvoice);
        InvoiceDTO requestDTO = new InvoiceDTO();

        // Act
        InvoiceDTO result = paypalInvoiceService.createInvoice(requestDTO);

        // Assert
        assertNotNull(result);
        verify(mockInvoice).create(apiContext);
        verify(invoiceAdapter).mapToInvoiceDTO(mockInvoice);
    }

    @Test
    void createInvoice_ThrowsException() throws PayPalRESTException {
        // Arrange
        InvoiceDTO requestDTO = new InvoiceDTO();
        when(invoiceAdapter.mapToInvoice(any(InvoiceDTO.class))).thenReturn(mockInvoice);
        doThrow(new PayPalRESTException("Error")).when(mockInvoice).create(any(APIContext.class));

        // Act & Assert
        PayPalInvoiceException exception = assertThrows(
            PayPalInvoiceException.class,
            () -> paypalInvoiceService.createInvoice(requestDTO)
        );
        assertEquals("Error creating invoice", exception.getMessage());
    }

    @Test
    void sendInvoice_Success() throws PayPalRESTException {
        // Act
        paypalInvoiceService.sendInvoice(INVOICE_ID);

        // Assert
        verify(mockInvoice).send(apiContext);
    }

    @Test
    void getInvoice_Success() {
        // Act
        Invoice result = paypalInvoiceService.getInvoice(INVOICE_ID);

        // Assert
        assertNotNull(result);
        assertEquals(mockInvoice, result);
    }

    @Test
    void getAllInvoices_Success() {
        // Arrange
        Invoices mockInvoices = mock(Invoices.class);
        List<Invoice> invoiceList = Arrays.asList(mockInvoice);
        when(mockInvoices.getInvoices()).thenReturn(invoiceList);
        invoiceMockedStatic.when(() -> Invoice.getAll(any(APIContext.class))).thenReturn(mockInvoices);
        when(invoiceAdapter.mapToInvoiceDTO(any(Invoice.class))).thenReturn(mockInvoiceDTO);

        // Act
        List<InvoiceDTO> result = paypalInvoiceService.getAllInvoices();

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void updateInvoice_Success() throws PayPalRESTException {
        // Arrange
        when(invoiceAdapter.mapToInvoice(any(InvoiceDTO.class))).thenReturn(mockInvoice);
        when(mockInvoice.update(apiContext)).thenReturn(mockInvoice); // Assure que update retourne un Invoice
        doReturn(mockInvoiceDTO).when(invoiceAdapter).mapToInvoiceDTO(any(Invoice.class));
        
        InvoiceDTO requestDTO = new InvoiceDTO();
    
        // Act
        InvoiceDTO result = paypalInvoiceService.updateInvoice(requestDTO);
    
        // Assert
        assertNotNull(result);
        verify(mockInvoice).update(apiContext);
        verify(invoiceAdapter).mapToInvoiceDTO(mockInvoice);
    }
    

    @Test
    void deleteInvoice_Success() throws PayPalRESTException {
        // Act
        paypalInvoiceService.deleteInvoice(INVOICE_ID);

        // Assert
        verify(mockInvoice).delete(apiContext);
    }

    @Test
    void cancelInvoice_Success() throws PayPalRESTException {
        // Arrange
        String reason = "Test cancellation";

        // Act
        paypalInvoiceService.cancelInvoice(INVOICE_ID, reason);

        // Assert
        verify(mockInvoice).cancel(eq(apiContext), any(CancelNotification.class));
    }

    @Test
    void getInvoiceDTO_Success() {
        // Arrange
        when(invoiceAdapter.mapToInvoiceDTO(any(Invoice.class))).thenReturn(mockInvoiceDTO);

        // Act
        InvoiceDTO result = paypalInvoiceService.getInvoiceDTO(INVOICE_ID);

        // Assert
        assertNotNull(result);
        verify(invoiceAdapter).mapToInvoiceDTO(mockInvoice);
    }
}