package com.trinity.user.domain.events;

import com.trinity.user.domain.enums.EmployeeRole;
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
