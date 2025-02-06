package com.trinity.authentication.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {

        SecurityContext context = SecurityContextHolder.getContext();

        Authentication authentication = context.getAuthentication();

        String name = authentication.getName();

        return ResponseEntity.ok("Hello, " + name + "!");
    } 
}
