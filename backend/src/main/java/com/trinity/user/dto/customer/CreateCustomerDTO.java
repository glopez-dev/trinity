package com.trinity.user.dto.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating a new customer")
public class CreateCustomerDTO {

    @Schema(description = "Customer's first name", example = "Jahn", required = true)
    @NotBlank(message = "First name is required")
    private String firstName;

    @Schema(description = "Customer's last name", example = "Smith", required = true)
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "Customer's email address", example = "customer@example.com", required = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Schema(description = "Customer's password", example = "customer123", required = true)
    @NotBlank(message = "Password is required")
    private String password;

}
