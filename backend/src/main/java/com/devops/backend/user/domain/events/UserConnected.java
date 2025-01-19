package com.devops.backend.user.domain.events;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class UserConnected implements UserEvent {
    UUID userId;
    String ipAddress;
    String userAgent;
    LocalDateTime timestamp;
}