package com.trinity.user.infrastructure.persistence.adapter;

import com.trinity.user.domain.model.Customer;
import com.trinity.user.domain.port.CustomerRepositoryPort;
import com.trinity.user.infrastructure.persistence.entity.CustomerJpaEntity;
import com.trinity.user.infrastructure.persistence.mapper.CustomerPersistenceMapper;
import com.trinity.user.infrastructure.persistence.repository.JpaCustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link CustomerRepositoryPort} over Spring Data.
 * An existing-by-id aggregate is synced in-place (preserving id/version/timestamps);
 * a new one is inserted directly so {@code @GeneratedValue} assigns the id, which
 * the returned domain object carries (the JWT depends on it).
 */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {

    private final JpaCustomerRepository jpaCustomerRepository;
    private final CustomerPersistenceMapper mapper;

    @Override
    public Customer save(Customer customer) {
        if (customer.getId() != null) {
            Optional<CustomerJpaEntity> managed = jpaCustomerRepository.findById(customer.getId());
            if (managed.isPresent()) {
                CustomerJpaEntity target = managed.get();
                mapper.updateEntity(target, customer);
                return mapper.toDomain(jpaCustomerRepository.save(target));
            }
        }
        return mapper.toDomain(jpaCustomerRepository.save(mapper.toEntity(customer)));
    }

    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaCustomerRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return jpaCustomerRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaCustomerRepository.existsByEmail(email);
    }

    @Override
    public List<Customer> findAll() {
        return jpaCustomerRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(Customer customer) {
        if (customer.getId() != null) {
            jpaCustomerRepository.deleteById(customer.getId());
        } else {
            jpaCustomerRepository.findByEmail(customer.getEmail()).ifPresent(jpaCustomerRepository::delete);
        }
    }
}
