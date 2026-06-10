package com.trinity.authentication.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.trinity.authentication.interfaces.rest.dto.AuthenticationResponse;
import com.trinity.authentication.interfaces.rest.dto.RegisterRequest;
import com.trinity.authentication.interfaces.rest.dto.LoginRequest;
import com.trinity.user.domain.model.EmployeeRole;
import com.trinity.user.domain.model.UserType;
import com.trinity.authentication.infrastructure.security.AppUserDetails;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

import org.springframework.security.core.userdetails.UserDetails;


class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private EmployeeRepositoryPort employeeRepository;

    @Mock
    private CustomerRepositoryPort customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterEmployee() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(EmployeeRole.EMPLOYEE);

        Employee employee = Employee.builder()
                .email(request.getEmail())
                .hashedPassword("encodedPassword")
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .type(UserType.EMPLOYEE)
                .build();

        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(jwtService.generateToken(any(AppUserDetails.class))).thenReturn("jwtToken");
        // When
        AuthenticationResponse response = authenticationService.registerEmployee(request);

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
                .type(UserType.EMPLOYEE)
                .build();

        UserDetails userDetails = new AppUserDetails(employee);
        when(userDetailsService.loadUserByUsername(request.getEmail())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwtToken");

        // When
        AuthenticationResponse response = authenticationService.login(request);

        // Then
        assertNotNull(response);
        assertEquals("jwtToken", response.getJwt());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(request.getEmail());
    }
}