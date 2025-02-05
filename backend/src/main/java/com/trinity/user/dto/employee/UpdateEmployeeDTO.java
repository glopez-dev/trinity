package com.trinity.user.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

import com.trinity.user.constant.EmployeeRole;
import com.trinity.user.constant.UserStatus;

@Data
@AllArgsConstructor
public class UpdateEmployeeDTO {

    private Optional<String> email;

    private Optional<String> firstName;

    private Optional<String> lastName;

    private Optional<EmployeeRole> role;

    private Optional<UserStatus> status;
}