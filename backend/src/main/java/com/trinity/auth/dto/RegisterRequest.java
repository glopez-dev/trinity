package com.trinity.auth.dto;

import com.trinity.auth.constant.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;

    private String firstName;
    private String lastName;

    private UserRole role;

}
