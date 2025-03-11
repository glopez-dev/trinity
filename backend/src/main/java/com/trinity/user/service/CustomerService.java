package com.trinity.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.trinity.user.constant.UserStatus;
import com.trinity.user.dto.customer.CreateCustomerDTO;
import com.trinity.user.dto.customer.ReadCustomerDTO;
import com.trinity.user.dto.customer.UpdateCustomerDTO;
import com.trinity.user.model.Customer;
import com.trinity.user.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ReadCustomerDTO createCustomer(CreateCustomerDTO createCustomerDTO) {
        if (customerRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Customer customer = Customer.builder()
                .firstName(createCustomerDTO.getFirstName())
                .lastName(createCustomerDTO.getLastName())
                .email(createCustomerDTO.getEmail())
                .hashedPassword(passwordEncoder.encode(createCustomerDTO.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return mapToReadCustomerDTO(savedCustomer);
    }

    @Transactional(readOnly = true)
    public ReadCustomerDTO getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        return mapToReadCustomerDTO(customer);
    }

    @Transactional(readOnly = true)
    public ReadCustomerDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + email));

        return mapToReadCustomerDTO(customer);
    }

    @Transactional(readOnly = true)
    public List<ReadCustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToReadCustomerDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReadCustomerDTO updateCustomer(UUID id, UpdateCustomerDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        if (customerUpdateDTO.getEmail() != null && !customerUpdateDTO.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(customerUpdateDTO.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            customer.setEmail(customerUpdateDTO.getEmail());
        }

        if (customerUpdateDTO.getFirstName() != null) {
            customer.setFirstName(customerUpdateDTO.getFirstName());
        }

        if (customerUpdateDTO.getLastName() != null) {
            customer.setLastName(customerUpdateDTO.getLastName());
        }

        if (customerUpdateDTO.getPassword() != null) {
            customer.setHashedPassword(passwordEncoder.encode(customerUpdateDTO.getPassword()));
        }

        Customer updatedCustomer = customerRepository.save(customer);

        return mapToReadCustomerDTO(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        customer.setStatusInactive();
        customerRepository.save(customer);
    }

    @Transactional
    public ReadCustomerDTO activateCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + id));

        customer.setStatusActive();
        Customer activatedCustomer = customerRepository.save(customer);

        return mapToReadCustomerDTO(activatedCustomer);
    }

    private ReadCustomerDTO mapToReadCustomerDTO(Customer customer) {
        return ReadCustomerDTO.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .lastLoginAt(customer.getLastLoginAt())
                .status(customer.getStatus())
                .type(customer.getType())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .build();
    }
}
