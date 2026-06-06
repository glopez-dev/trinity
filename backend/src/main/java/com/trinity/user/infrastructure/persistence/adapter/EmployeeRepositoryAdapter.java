package com.trinity.user.infrastructure.persistence.adapter;

import com.trinity.user.domain.model.Employee;
import com.trinity.user.domain.port.EmployeeRepositoryPort;
import com.trinity.user.infrastructure.persistence.entity.EmployeeJpaEntity;
import com.trinity.user.infrastructure.persistence.mapper.EmployeePersistenceMapper;
import com.trinity.user.infrastructure.persistence.repository.JpaEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence adapter implementing {@link EmployeeRepositoryPort} over Spring Data.
 * Mirrors {@link CustomerRepositoryAdapter}: in-place update when id is present,
 * direct insert otherwise (so the returned domain object carries the generated id).
 */
@Component
@RequiredArgsConstructor
public class EmployeeRepositoryAdapter implements EmployeeRepositoryPort {

    private final JpaEmployeeRepository jpaEmployeeRepository;
    private final EmployeePersistenceMapper mapper;

    @Override
    public Employee save(Employee employee) {
        if (employee.getId() != null) {
            Optional<EmployeeJpaEntity> managed = jpaEmployeeRepository.findById(employee.getId());
            if (managed.isPresent()) {
                EmployeeJpaEntity target = managed.get();
                mapper.updateEntity(target, employee);
                return mapper.toDomain(jpaEmployeeRepository.save(target));
            }
        }
        return mapper.toDomain(jpaEmployeeRepository.save(mapper.toEntity(employee)));
    }

    @Override
    public Optional<Employee> findById(UUID id) {
        return jpaEmployeeRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        return jpaEmployeeRepository.findByEmail(email).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaEmployeeRepository.existsByEmail(email);
    }

    @Override
    public List<Employee> findAll() {
        return jpaEmployeeRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void delete(Employee employee) {
        if (employee.getId() != null) {
            jpaEmployeeRepository.deleteById(employee.getId());
        } else {
            jpaEmployeeRepository.findByEmail(employee.getEmail()).ifPresent(jpaEmployeeRepository::delete);
        }
    }
}
