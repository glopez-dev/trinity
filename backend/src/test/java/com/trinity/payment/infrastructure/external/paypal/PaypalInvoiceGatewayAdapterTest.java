package com.trinity.payment.infrastructure.external.paypal;

import com.paypal.api.payments.CancelNotification;
import com.paypal.api.payments.Invoice;
import com.paypal.api.payments.Invoices;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.trinity.common.domain.exception.DomainException;
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

/**
 * Tests the PayPal anti-corruption gateway adapter: SDK calls (via mockStatic on
 * Invoice) and PayPalRESTException -> DomainException translation. The gateway
 * now speaks the domain {@link com.trinity.payment.domain.model.Invoice}; the
 * SDK mapping is delegated to the mocked {@link PaypalInvoiceAdapter}. The
 * domain<->SDK quirks are exercised by PaypalInvoiceAdapterTest.
 */
@ExtendWith(MockitoExtension.class)
class PaypalInvoiceGatewayAdapterTest {

    @Mock
    private PaypalInvoiceAdapter invoiceAdapter;

    @Mock
    private APIContext apiContext;

    @InjectMocks
    private PaypalInvoiceGatewayAdapter adapter;

    private Invoice mockInvoice;
    private com.trinity.payment.domain.model.Invoice mockDomainInvoice;
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
        mockDomainInvoice = com.trinity.payment.domain.model.Invoice.builder().build();
        invoiceMockedStatic.when(() -> Invoice.get(any(APIContext.class), eq(INVOICE_ID))).thenReturn(mockInvoice);
    }

    @Test
    void create_success() throws PayPalRESTException {
        when(invoiceAdapter.mapToSdkInvoice(any(com.trinity.payment.domain.model.Invoice.class)))
                .thenReturn(mockInvoice);
        when(invoiceAdapter.mapToInvoice(any(Invoice.class))).thenReturn(mockDomainInvoice);
        when(mockInvoice.create(any(APIContext.class))).thenReturn(mockInvoice);

        com.trinity.payment.domain.model.Invoice result =
                adapter.create(com.trinity.payment.domain.model.Invoice.builder().build());

        assertNotNull(result);
        verify(mockInvoice).create(apiContext);
        verify(invoiceAdapter).mapToInvoice(mockInvoice);
    }

    @Test
    void create_translatesSdkExceptionToDomainException() throws PayPalRESTException {
        when(invoiceAdapter.mapToSdkInvoice(any(com.trinity.payment.domain.model.Invoice.class)))
                .thenReturn(mockInvoice);
        doThrow(new PayPalRESTException("Error")).when(mockInvoice).create(any(APIContext.class));

        DomainException exception = assertThrows(DomainException.class,
                () -> adapter.create(com.trinity.payment.domain.model.Invoice.builder().build()));
        assertEquals("Error creating invoice", exception.getMessage());
    }

    @Test
    void send_success() throws PayPalRESTException {
        adapter.send(INVOICE_ID);
        verify(mockInvoice).send(apiContext);
    }

    @Test
    void get_success() {
        when(invoiceAdapter.mapToInvoice(any(Invoice.class))).thenReturn(mockDomainInvoice);

        com.trinity.payment.domain.model.Invoice result = adapter.get(INVOICE_ID);

        assertNotNull(result);
        verify(invoiceAdapter).mapToInvoice(mockInvoice);
    }

    @Test
    void getAll_success() {
        Invoices mockInvoices = mock(Invoices.class);
        List<Invoice> invoiceList = Arrays.asList(mockInvoice);
        when(mockInvoices.getInvoices()).thenReturn(invoiceList);
        invoiceMockedStatic.when(() -> Invoice.getAll(any(APIContext.class))).thenReturn(mockInvoices);
        when(invoiceAdapter.mapToInvoice(any(Invoice.class))).thenReturn(mockDomainInvoice);

        List<com.trinity.payment.domain.model.Invoice> result = adapter.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void getAll_nullInvoiceList_returnsEmpty() {
        Invoices mockInvoices = mock(Invoices.class);
        when(mockInvoices.getInvoices()).thenReturn(null);
        invoiceMockedStatic.when(() -> Invoice.getAll(any(APIContext.class))).thenReturn(mockInvoices);

        List<com.trinity.payment.domain.model.Invoice> result = adapter.getAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_success() throws PayPalRESTException {
        when(invoiceAdapter.mapToSdkInvoice(any(com.trinity.payment.domain.model.Invoice.class)))
                .thenReturn(mockInvoice);
        when(mockInvoice.update(apiContext)).thenReturn(mockInvoice);
        doReturn(mockDomainInvoice).when(invoiceAdapter).mapToInvoice(any(Invoice.class));

        com.trinity.payment.domain.model.Invoice result =
                adapter.update(com.trinity.payment.domain.model.Invoice.builder().build());

        assertNotNull(result);
        verify(mockInvoice).update(apiContext);
    }

    @Test
    void delete_success() throws PayPalRESTException {
        adapter.delete(INVOICE_ID);
        verify(mockInvoice).delete(apiContext);
    }

    @Test
    void cancel_success() throws PayPalRESTException {
        adapter.cancel(INVOICE_ID, "Test cancellation");
        verify(mockInvoice).cancel(eq(apiContext), any(CancelNotification.class));
    }
}
