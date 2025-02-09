package com.trinity.user.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;

@Data
@AllArgsConstructor
@Schema(description = "DTO for updating employee information")
public class UpdateEmployeeDTO {

    @Schema(description = "Employee email address", example = "employee@company.com")
    private Optional<String> email;

    @Schema(description = "Employee first name", example = "John")
    private Optional<String> firstName;

    @Schema(description = "Employee last name", example = "Doe")
    private Optional<String> lastName;

    @Schema(description = "Employee role in the system")
    private Optional<EmployeeRole> role;

    @Schema(description = "Employee status in the system")
    private Optional<UserStatus> status;
}