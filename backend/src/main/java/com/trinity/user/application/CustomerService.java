package com.trinity.user.application;

import java.util.List;
import java.util.UUID;

import com.trinity.user.application.command.CreateCustomerCommand;
import com.trinity.user.application.command.UpdateCustomerCommand;
import com.trinity.user.domain.model.UserStatus;
import com.trinity.user.domain.model.UserType;
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

    @Transactional
    public Customer createCustomer(CreateCustomerCommand command) {
        if (customerRepository.existsByEmail(command.email())) {
            throw new IllegalArgumentException("Email already in use");
        }

        Customer customer = Customer.builder()
                .firstName(command.firstName())
                .lastName(command.lastName())
                .email(command.email())
                .hashedPassword(passwordEncoder.encode(command.rawPassword()))
                .status(UserStatus.ACTIVE)
                .type(UserType.CUSTOMER)
                .build();

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerById(UUID id) {
        return findById(id);
    }

    @Transactional(readOnly = true)
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Customer not found with email: " + email));
    }

    @Transactional(readOnly = true)
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer updateCustomer(UUID id, UpdateCustomerCommand command) {
        Customer customer = findById(id);

        command.email().ifPresent(newEmail -> {
            if (!newEmail.equals(customer.getEmail())) {
                if (customerRepository.existsByEmail(newEmail)) {
                    throw new IllegalArgumentException("Email already in use");
                }
                customer.setEmail(newEmail);
            }
        });

        command.firstName().ifPresent(customer::setFirstName);
        command.lastName().ifPresent(customer::setLastName);
        command.rawPassword().ifPresent(rawPassword ->
                customer.setHashedPassword(passwordEncoder.encode(rawPassword)));

        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(UUID id) {
        Customer customer = findById(id);

        customer.deactivate();
        customerRepository.save(customer);
    }

    @Transactional
    public Customer activateCustomer(UUID id) {
        Customer customer = findById(id);

        customer.activate();
        return customerRepository.save(customer);
    }

    private Customer findById(UUID id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer not found with id: " + id));
    }
}
