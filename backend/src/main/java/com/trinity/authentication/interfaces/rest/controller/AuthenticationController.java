package com.trinity.authentication.interfaces.rest.controller;

import com.trinity.authentication.interfaces.rest.dto.CustomerRegisterRequest;
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

import com.trinity.authentication.interfaces.rest.dto.AuthenticationResponse;
import com.trinity.authentication.interfaces.rest.dto.LoginRequest;
import com.trinity.authentication.interfaces.rest.dto.RegisterRequest;
import com.trinity.authentication.application.AuthenticationService;
import com.trinity.authentication.application.command.LoginCommand;
import com.trinity.authentication.application.command.RegisterCustomerCommand;
import com.trinity.authentication.application.command.RegisterEmployeeCommand;

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
        String jwt = authService.registerEmployee(new RegisterEmployeeCommand(
                request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName()));
        return ResponseEntity.ok(AuthenticationResponse.builder().jwt(jwt).build());
    }

    @Operation(summary = "Register a new customer")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Customer registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    }
    )
    @PostMapping("/register/customer")
    public ResponseEntity<AuthenticationResponse> registerCustomer(@Valid @RequestBody CustomerRegisterRequest request) {
        String jwt = authService.registerCustomer(new RegisterCustomerCommand(
                request.getEmail(), request.getPassword(), request.getFirstName(), request.getLastName()));
        return ResponseEntity.ok(AuthenticationResponse.builder().jwt(jwt).build());
    }

    @Operation(summary = "Login to the application")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login successful"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody LoginRequest request) {
        String jwt = authService.login(new LoginCommand(request.getEmail(), request.getPassword()));
        return ResponseEntity.ok(AuthenticationResponse.builder().jwt(jwt).build());
    }

}
