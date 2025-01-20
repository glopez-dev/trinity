package com.trinity.auth.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.trinity.auth.constant.UserRole;
import com.trinity.auth.dto.AuthenticationResponse;
import com.trinity.auth.dto.LoginRequest;
import com.trinity.auth.dto.RegisterRequest;
import com.trinity.auth.model.Employee;
import com.trinity.auth.repository.EmployeeRepository;
import java.util.Optional;


class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");

        Employee employee = Employee.builder()
                .email(request.getEmail())
                .hashedPassword("encodedPassword")
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(UserRole.EMPLOYEE)
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(jwtService.generateToken(employee)).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getJwt());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testLogin() {
        // Given
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        Employee employee = Employee.builder()
                .email(request.getEmail())
                .hashedPassword("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.EMPLOYEE)
                .build();

        when(employeeRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(employee));
        when(jwtService.generateToken(employee)).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.login(request);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getJwt());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(employeeRepository, times(1)).findByEmail(request.getEmail());
    }
}