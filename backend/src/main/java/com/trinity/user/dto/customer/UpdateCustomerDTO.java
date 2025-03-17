package com.trinity.user.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerDTO {

    @Schema(description = "Customer's first name", example = "John")
    private Optional<String> firstName;

    @Schema(description = "Customer's last name", example = "Doe")
    private Optional<String> lastName;

    @Schema(description = "Customer's email address", example = "user@example.com")
    @Email(message = "Email should be valid")
    private Optional<String> email;

    @Schema(description = "Customer's password", example = "password123")
    private Optional<String> password;
}
