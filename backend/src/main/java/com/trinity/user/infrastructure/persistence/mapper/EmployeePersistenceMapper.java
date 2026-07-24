package com.trinity.user.infrastructure.persistence.mapper;

import com.trinity.user.domain.model.Employee;
import com.trinity.user.infrastructure.persistence.entity.EmployeeJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Hand-written translation between the {@link Employee} domain POJO and its anemic
 * JPA mirror. Same ownership rules as {@link CustomerPersistenceMapper}: toEntity
 * copies id+version (never timestamps), toDomain copies the full round-trip,
 * updateEntity syncs mutable fields in-place without touching id/version/timestamps.
 */
@Component
public class EmployeePersistenceMapper {

    public EmployeeJpaEntity toEntity(Employee d) {
        if (d == null) {
            return null;
        }
        return EmployeeJpaEntity.builder()
            .id(d.getId())
            .email(d.getEmail())
            .hashedPassword(d.getHashedPassword())
            .firstName(d.getFirstName())
            .lastName(d.getLastName())
            .type(d.getType())
            .lastLoginAt(d.getLastLoginAt())
            .status(d.getStatus())
            .version(d.getVersion())
            .hireDate(d.getHireDate())
            .terminationDate(d.getTerminationDate())
            .role(d.getRole())
            .build();
    }

    public Employee toDomain(EmployeeJpaEntity e) {
        if (e == null) {
            return null;
        }
        return Employee.builder()
            .id(e.getId())
            .email(e.getEmail())
            .hashedPassword(e.getHashedPassword())
            .firstName(e.getFirstName())
            .lastName(e.getLastName())
            .type(e.getType())
            .lastLoginAt(e.getLastLoginAt())
            .status(e.getStatus())
            .createdAt(e.getCreatedAt())
            .updatedAt(e.getUpdatedAt())
            .version(e.getVersion())
            .hireDate(e.getHireDate())
            .terminationDate(e.getTerminationDate())
            .role(e.getRole())
            .build();
    }

    /** In-place sync of a managed entity (update path); never touches id/version/timestamps. */
    public void updateEntity(EmployeeJpaEntity target, Employee source) {
        target.setEmail(source.getEmail());
        target.setHashedPassword(source.getHashedPassword());
        target.setFirstName(source.getFirstName());
        target.setLastName(source.getLastName());
        target.setType(source.getType());
        target.setLastLoginAt(source.getLastLoginAt());
        target.setStatus(source.getStatus());
        target.setHireDate(source.getHireDate());
        target.setTerminationDate(source.getTerminationDate());
        target.setRole(source.getRole());
    }
}
