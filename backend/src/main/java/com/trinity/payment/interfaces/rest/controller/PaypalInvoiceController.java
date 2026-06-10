package com.trinity.payment.interfaces.rest.controller;

import com.trinity.payment.interfaces.rest.dto.*;
import com.trinity.payment.interfaces.rest.mapper.InvoiceApiMapper;
import com.trinity.payment.application.PaypalInvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Paypal Invoice", description = "Operations related to Paypal invoices")
public class PaypalInvoiceController {

    private final PaypalInvoiceService invoiceService;
    private final InvoiceApiMapper invoiceMapper;

    @Operation(summary = "Create a new invoice", description = "Creates a new Paypal invoice")
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(
        @Valid @RequestBody InvoiceDTO request
    ) {
        InvoiceDTO invoice = invoiceMapper.toDTO(invoiceService.createInvoice(invoiceMapper.toDomain(request)));
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Get all invoices", description = "Retrieves a list of all Paypal invoices")
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices().stream()
                .map(invoiceMapper::toDTO)
                .toList();
        return ResponseEntity.ok(invoices);
    }

    @Operation(summary = "Get a specific invoice", description = "Retrieves a specific Paypal invoice by its ID")
    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoice(
        @PathVariable String invoiceId
    ) {
        InvoiceDTO invoice = invoiceMapper.toDTO(invoiceService.getInvoice(invoiceId));
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Update an invoice", description = "Updates an existing Paypal invoice")
    @PutMapping
    public ResponseEntity<InvoiceDTO> updateInvoice(
        @Valid @RequestBody InvoiceDTO request
    ) {
        InvoiceDTO invoice = invoiceMapper.toDTO(invoiceService.updateInvoice(invoiceMapper.toDomain(request)));
        return ResponseEntity.ok(invoice);
    }

    @Operation(summary = "Delete an invoice", description = "Deletes a specific Paypal invoice by its ID")
    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(
        @PathVariable String invoiceId
    ) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Send an invoice", description = "Sends a specific Paypal invoice to the recipient")
    @PostMapping("/{invoiceId}/send")
    public ResponseEntity<Void> sendInvoice(
        @PathVariable String invoiceId
    ) {
        invoiceService.sendInvoice(invoiceId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Cancel an invoice", description = "Cancels a specific Paypal invoice with a given reason")
    @PostMapping("/{invoiceId}/cancel")
    public ResponseEntity<Void> cancelInvoice(
        @PathVariable String invoiceId,
        @RequestParam String reason
    ) {
        invoiceService.cancelInvoice(invoiceId, reason);
        return ResponseEntity.ok().build();
    }
}