package com.trinity.authentication.service;

import com.trinity.user.infrastructure.security.AppUserDetails;
import com.trinity.user.domain.model.User;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.trinity.user.domain.port.EmployeeRepositoryPort;

import lombok.AllArgsConstructor;

@Service("customUserDetailsService")
@AllArgsConstructor
class CustomUserDetailsService implements UserDetailsService {

    private final EmployeeRepositoryPort employeeRepository;
    private final CustomerRepositoryPort customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = employeeRepository.findByEmail(email)
                .map(employee -> (User) employee)
                .orElseGet(() ->
                        customerRepository.findByEmail(email)
                                .orElseThrow(() ->
                                        new UsernameNotFoundException("User not found with email: " + email)
                                )
                );
        return new AppUserDetails(user);
    }
}
