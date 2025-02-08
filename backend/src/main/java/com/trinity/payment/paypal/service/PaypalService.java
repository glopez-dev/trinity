package com.trinity.payment.paypal.service;

import org.springframework.stereotype.Service;

import com.paypal.base.rest.APIContext;
import com.trinity.payment.paypal.adapter.PaypalInvoiceAdapter;
import com.trinity.payment.paypal.config.PaypalConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaypalService {
    private final PaypalConfig paypalConfig;
    private final PaypalInvoiceAdapter invoiceAdapter;

    //check paypal authorization with the given credentials
    public APIContext checkAuthorization() {
        return paypalConfig.getAPIContext();
    }
}
