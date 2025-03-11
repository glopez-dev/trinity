package com.trinity.user.dto.customer;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCustomerDTO {

    private String firstName;
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String password;
}
