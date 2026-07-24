package com.trinity.user.domain.model;

import java.time.Instant;

import com.trinity.common.domain.exception.BusinessRuleViolation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate for an employee, carrying a role and an employment lifecycle.
 */
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends User {

    @Builder.Default
    private Instant hireDate = Instant.now();

    private Instant terminationDate;

    @Builder.Default
    private EmployeeRole role = EmployeeRole.EMPLOYEE;

    @Builder.Default
    private UserType type = UserType.EMPLOYEE;

    /* Domain behavior */

    /** Updates names, ignoring null values (partial update). */
    public void rename(String firstName, String lastName) {
        if (firstName != null) {
            this.setFirstName(firstName);
        }
        if (lastName != null) {
            this.setLastName(lastName);
        }
    }

    public void changeRole(EmployeeRole role) {
        if (role == null) {
            throw new BusinessRuleViolation("Employee role must not be null");
        }
        this.role = role;
    }

    public void terminate(Instant terminationDate) {
        if (terminationDate == null) {
            throw new BusinessRuleViolation("Termination date must not be null");
        }
        if (this.hireDate != null && terminationDate.isBefore(this.hireDate)) {
            throw new BusinessRuleViolation("Termination date must not be before hire date");
        }
        this.terminationDate = terminationDate;
    }

    @Override
    public UserType getType() {
        return this.type;
    }
}
