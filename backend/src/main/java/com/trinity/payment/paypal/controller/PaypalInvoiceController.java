package com.trinity.payment.paypal.controller;


import com.trinity.payment.paypal.dto.*;
import com.trinity.payment.paypal.service.PaypalInvoiceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class PaypalInvoiceController {

    private final PaypalInvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(
        @Valid @RequestBody InvoiceDTO request
    ) {
        InvoiceDTO invoice = invoiceService.createInvoice(request);
        return ResponseEntity.ok(invoice);
    }

    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDTO> getInvoice(
        @PathVariable String invoiceId
    ) {
        InvoiceDTO invoice = invoiceService.getInvoiceDTO(invoiceId);
        return ResponseEntity.ok(invoice);
    }

    @PutMapping
    public ResponseEntity<InvoiceDTO> updateInvoice(
        @Valid @RequestBody InvoiceDTO request
    ) {
        InvoiceDTO invoice = invoiceService.updateInvoice(request);
        return ResponseEntity.ok(invoice);
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<Void> deleteInvoice(
        @PathVariable String invoiceId
    ) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/send")
    public ResponseEntity<Void> sendInvoice(
        @PathVariable String invoiceId
    ) {
        invoiceService.sendInvoice(invoiceId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{invoiceId}/cancel")
    public ResponseEntity<Void> cancelInvoice(
        @PathVariable String invoiceId,
        @RequestParam String reason
    ) {
        invoiceService.cancelInvoice(invoiceId, reason);
        return ResponseEntity.ok().build();
    }
}