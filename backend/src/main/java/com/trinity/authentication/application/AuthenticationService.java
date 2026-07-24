package com.trinity.authentication.application;

import com.trinity.authentication.application.command.LoginCommand;
import com.trinity.authentication.application.command.RegisterCustomerCommand;
import com.trinity.authentication.application.command.RegisterEmployeeCommand;
import com.trinity.authentication.infrastructure.security.AppUserDetails;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /** @return the JWT for the newly registered employee */
    @Transactional
    public String registerEmployee(RegisterEmployeeCommand command) {

        if (employeeRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Employee employee = Employee.builder()
                .email(command.email())
                .hashedPassword(passwordEncoder.encode(command.rawPassword()))
                .firstName(command.firstName())
                .lastName(command.lastName())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);

        return jwtService.generateToken(new AppUserDetails(savedEmployee));
    }

    /** @return the JWT for the newly registered customer */
    @Transactional
    public String registerCustomer(RegisterCustomerCommand command) {

        if (customerRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Customer customer = Customer.builder()
                .email(command.email())
                .hashedPassword(passwordEncoder.encode(command.rawPassword()))
                .firstName(command.firstName())
                .lastName(command.lastName())
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return jwtService.generateToken(new AppUserDetails(savedCustomer));
    }

    /** @return the JWT for the authenticated user */
    public String login(LoginCommand command) {

        this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                command.email(),
                command.password()
            )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(command.email());

        return jwtService.generateToken(userDetails);
    }

}
