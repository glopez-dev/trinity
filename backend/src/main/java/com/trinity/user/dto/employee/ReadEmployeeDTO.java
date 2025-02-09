package com.trinity.user.dto.employee;

import java.time.Instant;
import java.util.UUID;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;
import com.trinity.user.model.Employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Employee Data Transfer Object for reading employee information")
public class ReadEmployeeDTO {

    @Schema(description = "Type of user")
    private UserType type;

    @Schema(description = "Current status of the user")
    private UserStatus status;

    @Schema(description = "Timestamp when the user was created")
    private Instant createdAt;

    @Schema(description = "Timestamp of last user update")
    private Instant updatedAt;

    @Schema(description = "Unique identifier of the employee")
    private UUID id;

    @Schema(description = "Employee's email address")
    private String email;

    @Schema(description = "Employee's first name")
    private String firstName;

    @Schema(description = "Employee's last name")
    private String lastName;

    @Schema(description = "Employee's role in the organization")
    private EmployeeRole role;

    @Schema(description = "Timestamp of employee's last login")
    private Instant lastLoginAt;

    @Schema(description = "Date when the employee was hired")
    private Instant hireDate;

    @Schema(description = "Date when the employee was terminated, if applicable")
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
