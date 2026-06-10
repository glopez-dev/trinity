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

import com.trinity.authentication.application.command.LoginCommand;
import com.trinity.authentication.application.command.RegisterEmployeeCommand;
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
        RegisterEmployeeCommand request = new RegisterEmployeeCommand(
                "test@example.com", "password", "John", "Doe");

        Employee employee = Employee.builder()
                .email(request.email())
                .hashedPassword("encodedPassword")
                .firstName(request.firstName())
                .lastName(request.lastName())
                .role(EmployeeRole.EMPLOYEE)
                .type(UserType.EMPLOYEE)
                .build();

        when(passwordEncoder.encode(request.rawPassword())).thenReturn("encodedPassword");
        when(employeeRepository.existsByEmail(request.email())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(jwtService.generateToken(any(AppUserDetails.class))).thenReturn("jwtToken");
        // When
        String jwt = authenticationService.registerEmployee(request);

        // Then
        assertEquals("jwtToken", jwt);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testLogin() {
        // Given
        LoginCommand request = new LoginCommand("test@example.com", "password");

        Employee employee = Employee.builder()
                .email(request.email())
                .hashedPassword("encodedPassword")
                .firstName("John")
                .lastName("Doe")
                .type(UserType.EMPLOYEE)
                .build();

        UserDetails userDetails = new AppUserDetails(employee);
        when(userDetailsService.loadUserByUsername(request.email())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("jwtToken");

        // When
        String jwt = authenticationService.login(request);

        // Then
        assertEquals("jwtToken", jwt);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername(request.email());
    }
}