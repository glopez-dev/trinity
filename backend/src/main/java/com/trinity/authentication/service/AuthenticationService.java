package com.trinity.authentication.service;

import com.trinity.authentication.dto.CustomerRegisterRequest;
import com.trinity.user.infrastructure.security.AppUserDetails;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trinity.authentication.dto.AuthenticationResponse;
import com.trinity.authentication.dto.LoginRequest;
import com.trinity.authentication.dto.RegisterRequest;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.EmployeeRepositoryPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeRepositoryPort employeeRepository;
    private final CustomerRepositoryPort customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Transactional
    public AuthenticationResponse registerEmployee(RegisterRequest request) {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Employee employee = Employee.builder()
                .email(request.getEmail())
                .hashedPassword(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        String jwtToken = jwtService.generateToken(new AppUserDetails(savedEmployee));

        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();

    }

    @Transactional
    public AuthenticationResponse registerCustomer(@Valid CustomerRegisterRequest request) {

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Customer customer = Customer.builder()
                .email(request.getEmail())
                .hashedPassword(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        String jwtToken = jwtService.generateToken(new AppUserDetails(savedCustomer));

        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {

        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String jwtToken = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }

}
