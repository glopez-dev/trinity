package com.trinity.user.infrastructure.persistence;

import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.model.User;
import com.trinity.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaUserRepository implements UserRepository {
    private final CustomerJpaRepository customerRepository;
    private final EmployeeJpaRepository employeeRepository;

    @Override
    public Optional<User> findById(UUID id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            return Optional.of(customer.get());
        }
        return employeeRepository.findById(id).map(employee -> employee);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<Customer> customer = customerRepository.findByEmail(email);
        if (customer.isPresent()) {
            return Optional.of(customer.get());
        }
        return employeeRepository.findByEmail(email).map(employee -> employee);
    }

    @Override
    public User save(User user) {
        if (user instanceof Customer) {
            return customerRepository.save((Customer) user);
        } else if (user instanceof Employee) {
            return employeeRepository.save((Employee) user);
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    @Override
    public void delete(User user) {
        if (user instanceof Customer) {
            customerRepository.delete((Customer) user);
        } else if (user instanceof Employee) {
            employeeRepository.delete((Employee) user);
        }
    }
}
