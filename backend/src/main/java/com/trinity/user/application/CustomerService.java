package com.trinity.user.application;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
import com.trinity.user.interfaces.rest.dto.CreateCustomerRequest;
import com.trinity.user.interfaces.rest.dto.CustomerResponse;
import com.trinity.user.interfaces.rest.dto.UpdateCustomerRequest;
import com.trinity.user.interfaces.rest.mapper.CustomerApiMapper;
import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import com.trinity.common.domain.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepositoryPort customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerApiMapper customerApiMapper;

    @Transactional
    public CustomerResponse createCustomer(CreateCustomerRequest createCustomerDTO) {
        if (customerRepository.existsByEmail(createCustomerDTO.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Customer customer = Customer.builder()
                .firstName(createCustomerDTO.getFirstName())
                .lastName(createCustomerDTO.getLastName())
                .email(createCustomerDTO.getEmail())
                .hashedPassword(passwordEncoder.encode(createCustomerDTO.getPassword()))
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .build();

        Customer savedCustomer = customerRepository.save(customer);

        return customerApiMapper.toResponse(savedCustomer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        return customerApiMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public CustomerResponse getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer not found with email: " + email));

        return customerApiMapper.toResponse(customer);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerApiMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponse updateCustomer(UUID id, UpdateCustomerRequest customerUpdateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        if (customerUpdateDTO.getEmail() != null && customerUpdateDTO.getEmail().isPresent()) {
            String newEmail = customerUpdateDTO.getEmail().get();
            if (!newEmail.equals(customer.getEmail())) {
                if (customerRepository.existsByEmail(newEmail)) {
                    throw new IllegalArgumentException("Email already in use");
                }
                customer.setEmail(newEmail);
            }
        }

        if (customerUpdateDTO.getFirstName() != null && customerUpdateDTO.getFirstName().isPresent()) {
            customer.setFirstName(customerUpdateDTO.getFirstName().get());
        }

        if (customerUpdateDTO.getLastName() != null && customerUpdateDTO.getLastName().isPresent()) {
            customer.setLastName(customerUpdateDTO.getLastName().get());
        }

        if (customerUpdateDTO.getPassword() != null && customerUpdateDTO.getPassword().isPresent()) {
            String newPassword = customerUpdateDTO.getPassword().get();
            customer.setHashedPassword(passwordEncoder.encode(newPassword));
        }

        Customer updatedCustomer = customerRepository.save(customer);

        return customerApiMapper.toResponse(updatedCustomer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        customer.deactivate();
        customerRepository.save(customer);
    }

    @Transactional
    public CustomerResponse activateCustomer(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));

        customer.activate();
        Customer activatedCustomer = customerRepository.save(customer);

        return customerApiMapper.toResponse(activatedCustomer);
    }
}
