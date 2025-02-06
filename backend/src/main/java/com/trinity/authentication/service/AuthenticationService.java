package com.trinity.authentication.service;

import java.time.Instant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.authentication.dto.AuthenticationResponse;
import com.trinity.authentication.dto.LoginRequest;
import com.trinity.authentication.dto.RegisterRequest;
import com.trinity.user.model.Employee;
import com.trinity.user.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; 
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        Employee employee = Employee.builder()
                .email(request.getEmail())
                .hashedPassword(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        String jwtToken = jwtService.generateToken(savedEmployee);

        return new AuthenticationResponse(jwtToken);

    }

    public AuthenticationResponse login(LoginRequest request) {

        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        Employee employee = employeeRepository
            .findByEmail(request.getEmail())
            .orElseThrow();

        employee.setLastLoginAt(Instant.now());
        employeeRepository.save(employee);

        String jwtToken = jwtService.generateToken(employee);

        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }
    
}
