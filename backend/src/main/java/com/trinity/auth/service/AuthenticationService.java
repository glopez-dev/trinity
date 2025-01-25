package com.trinity.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.trinity.auth.constant.UserRole;
import com.trinity.auth.dto.AuthenticationResponse;
import com.trinity.auth.dto.LoginRequest;
import com.trinity.auth.dto.RegisterRequest;
import com.trinity.auth.model.Employee;
import com.trinity.auth.repository.EmployeeRepository;

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
                .role(UserRole.EMPLOYEE)
                .build();

        employeeRepository.save(employee);

        String jwtToken = jwtService.generateToken(employee);

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

        Employee employee = employeeRepository
            .findByEmail(request.getEmail())
            .orElseThrow();

        String jwtToken = jwtService.generateToken(employee);

        return AuthenticationResponse.builder()
                .jwt(jwtToken)
                .build();
    }
    
}
