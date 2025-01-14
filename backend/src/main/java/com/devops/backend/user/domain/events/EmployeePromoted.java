package com.devops.backend.user.domain.events;

import com.devops.backend.user.domain.enums.EmployeeRole;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class EmployeePromoted implements UserEvent {
    UUID userId;
    EmployeeRole initialRole;
    EmployeeRole newRole;
    LocalDateTime timestamp;
}
