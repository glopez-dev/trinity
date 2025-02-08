package com.trinity.payment.paypal.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.APIContext;
import com.trinity.payment.paypal.service.PaypalService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/paypal")
@RequiredArgsConstructor
public class PaypalController {

    private final PaypalService paypal;

    //check authorization
    @GetMapping("/check")
    public ResponseEntity<APIContext> checkAuthorization() {
        return ResponseEntity.ok(paypal.checkAuthorization());
    }
}