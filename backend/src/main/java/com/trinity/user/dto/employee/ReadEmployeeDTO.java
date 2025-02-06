package com.trinity.user.dto.employee;

import java.time.Instant;
import java.util.UUID;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
import com.trinity.user.model.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReadEmployeeDTO {

    // User fields
    private UserType type;
    private UserStatus status;
    private Instant createdAt;
    private Instant updatedAt;

    // Employee fields
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private EmployeeRole role;

    private Instant lastLoginAt;

    private Instant hireDate;
    private Instant terminationDate;

    public ReadEmployeeDTO(Employee employee) {
        this.id = employee.getId();
        this.email = employee.getEmail();
        this.firstName = employee.getFirstName();
        this.lastName = employee.getLastName();
        this.role = employee.getRole();
        this.type = employee.getType();
        this.status = employee.getStatus();
        this.createdAt = employee.getCreatedAt();
        this.updatedAt = employee.getUpdatedAt();
        this.lastLoginAt = employee.getLastLoginAt();
        this.hireDate = employee.getHireDate();
        this.terminationDate = employee.getTerminationDate();
    }

}
