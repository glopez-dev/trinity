package com.trinity.authentication.controller;

import com.trinity.authentication.dto.CustomerRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trinity.authentication.dto.AuthenticationResponse;
import com.trinity.authentication.dto.LoginRequest;
import com.trinity.authentication.dto.RegisterRequest;
import com.trinity.authentication.service.AuthenticationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication Routes")
public class AuthenticationController {

    private final AuthenticationService authService;

    public AuthenticationController(AuthenticationService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new employee")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Employee registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    }
    )
    @PostMapping("/register/employee")
    public ResponseEntity<AuthenticationResponse> registerEmployee(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerEmployee(request));
    }

    @Operation(summary = "Register a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    }
    )
    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@Valid @RequestBody CustomerRegisterRequest request) {
        return ResponseEntity.ok(authService.registerCustomer(request));
    }

    @Operation(summary = "Login to the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
