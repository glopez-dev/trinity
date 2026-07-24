package com.trinity.authentication.interfaces.rest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trinity.authentication.application.AuthenticationService;
import com.trinity.authentication.application.command.LoginCommand;
import com.trinity.authentication.application.command.RegisterCustomerCommand;
import com.trinity.authentication.application.command.RegisterEmployeeCommand;
import com.trinity.authentication.interfaces.rest.dto.AuthenticationResponse;
import com.trinity.authentication.interfaces.rest.dto.CustomerRegisterRequest;
import com.trinity.authentication.interfaces.rest.dto.LoginRequest;
import com.trinity.authentication.interfaces.rest.dto.RegisterRequest;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerEmployee_ValidRequest_ReturnsJwt() {
        RegisterRequest request = RegisterRequest.builder()
                .email("employee@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .build();
        when(authService.registerEmployee(any(RegisterEmployeeCommand.class))).thenReturn("jwtToken");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerEmployee(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getJwt());
        verify(authService).registerEmployee(any(RegisterEmployeeCommand.class));
    }

    @Test
    void registerCustomer_ValidRequest_ReturnsJwt() {
        CustomerRegisterRequest request = CustomerRegisterRequest.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("customer@example.com")
                .password("password123")
                .build();
        when(authService.registerCustomer(any(RegisterCustomerCommand.class))).thenReturn("jwtToken");

        ResponseEntity<AuthenticationResponse> response = authenticationController.registerCustomer(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getJwt());
        verify(authService).registerCustomer(any(RegisterCustomerCommand.class));
    }

    @Test
    void login_ValidCredentials_ReturnsJwt() {
        LoginRequest request = LoginRequest.builder()
                .email("user@example.com")
                .password("password123")
                .build();
        when(authService.login(any(LoginCommand.class))).thenReturn("jwtToken");

        ResponseEntity<AuthenticationResponse> response = authenticationController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwtToken", response.getBody().getJwt());
        verify(authService).login(any(LoginCommand.class));
    }
}
