package com.trinity.payment.paypal.controller;

import com.trinity.payment.paypal.dto.InvoiceDTO;
import com.trinity.payment.paypal.service.PaypalInvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaypalInvoiceControllerTest {

    @Mock
    private PaypalInvoiceService invoiceService;

    @InjectMocks
    private PaypalInvoiceController invoiceController;

    private InvoiceDTO sampleInvoiceDTO;
    private static final String INVOICE_ID = "INV2-QXWN-W3VH-Q8H7-XH8J";

    @BeforeEach
    void setUp() {
        sampleInvoiceDTO = new InvoiceDTO();
        // Set up your sample DTO with necessary fields
    }

    @Test
    void createInvoice_ShouldReturnCreatedInvoice() {
        // Arrange
        when(invoiceService.createInvoice(any(InvoiceDTO.class))).thenReturn(sampleInvoiceDTO);

        // Act
        ResponseEntity<InvoiceDTO> response = invoiceController.createInvoice(sampleInvoiceDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleInvoiceDTO, response.getBody());
        verify(invoiceService, times(1)).createInvoice(any(InvoiceDTO.class));
    }

    @SuppressWarnings("null")
    @Test
    void getAllInvoices_ShouldReturnListOfInvoices() {
        // Arrange
        List<InvoiceDTO> invoices = Arrays.asList(sampleInvoiceDTO, sampleInvoiceDTO);
        when(invoiceService.getAllInvoices()).thenReturn(invoices);

        // Act
        ResponseEntity<List<InvoiceDTO>> response = invoiceController.getAllInvoices();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        verify(invoiceService, times(1)).getAllInvoices();
    }

    @Test
    void getInvoice_ShouldReturnInvoice() {
        // Arrange
        when(invoiceService.getInvoiceDTO(INVOICE_ID)).thenReturn(sampleInvoiceDTO);

        // Act
        ResponseEntity<InvoiceDTO> response = invoiceController.getInvoice(INVOICE_ID);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleInvoiceDTO, response.getBody());
        verify(invoiceService, times(1)).getInvoiceDTO(INVOICE_ID);
    }

    @Test
    void updateInvoice_ShouldReturnUpdatedInvoice() {
        // Arrange
        when(invoiceService.updateInvoice(any(InvoiceDTO.class)))
            .thenReturn(sampleInvoiceDTO);

        // Act
        ResponseEntity<InvoiceDTO> response = invoiceController.updateInvoice(sampleInvoiceDTO);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertEquals(sampleInvoiceDTO, response.getBody());
        verify(invoiceService, times(1)).updateInvoice(any(InvoiceDTO.class));
    }

    @Test
    void deleteInvoice_ShouldReturnSuccessResponse() {
        // Arrange
        doNothing().when(invoiceService).deleteInvoice(INVOICE_ID);

        // Act
        ResponseEntity<Void> response = invoiceController.deleteInvoice(INVOICE_ID);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(invoiceService, times(1)).deleteInvoice(INVOICE_ID);
    }

    @Test
    void sendInvoice_ShouldReturnSuccessResponse() {
        // Arrange
        doNothing().when(invoiceService).sendInvoice(INVOICE_ID);

        // Act
        ResponseEntity<Void> response = invoiceController.sendInvoice(INVOICE_ID);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(invoiceService, times(1)).sendInvoice(INVOICE_ID);
    }

    @Test
    void cancelInvoice_ShouldReturnSuccessResponse() {
        // Arrange
        String cancelReason = "Payment method changed";
        doNothing().when(invoiceService).cancelInvoice(INVOICE_ID, cancelReason);

        // Act
        ResponseEntity<Void> response = invoiceController.cancelInvoice(INVOICE_ID, cancelReason);

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        verify(invoiceService, times(1)).cancelInvoice(INVOICE_ID, cancelReason);
    }
}