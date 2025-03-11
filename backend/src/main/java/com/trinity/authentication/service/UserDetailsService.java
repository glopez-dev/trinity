package com.trinity.authentication.service;

import com.trinity.user.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trinity.user.repository.EmployeeRepository;

import lombok.AllArgsConstructor;

@Service("customUserDetailsService")
@AllArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return employeeRepository.findByEmail(email)
                .map(employee -> (UserDetails) employee)
                .orElseGet(() ->
                        customerRepository.findByEmail(email)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException("User not found with email: " + email)
                                )
                );
    }
}
