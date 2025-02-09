package com.trinity.user.dto.employee;

import java.time.Instant;

import com.trinity.user.constant.EmployeeRole;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO for creating a new employee")
public class CreateEmployeeDTO {

    @Schema(description = "Employee's email address", example = "john.doe@company.com", required = true)
    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Schema(description = "Employee's password", example = "password123", required = true)
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @Schema(description = "Employee's first name", example = "John", required = true)
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @Schema(description = "Employee's last name", example = "Doe", required = true)
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Schema(description = "Employee's role in the organization", required = true)
    @NotNull(message = "Role cannot be null")
    private EmployeeRole role;
 
    @Schema(description = "Date when the employee was hired", example = "2023-01-01T00:00:00Z")
    private Instant hireDate;
}
