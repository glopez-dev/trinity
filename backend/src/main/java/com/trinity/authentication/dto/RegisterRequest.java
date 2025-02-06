package com.trinity.authentication.dto;

import com.trinity.user.constant.EmployeeRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @NotEmpty(message = "Email cannot be empty")
    @Email
    private String email;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @NotNull(message = "Role cannot be null")
    private EmployeeRole role;
}
