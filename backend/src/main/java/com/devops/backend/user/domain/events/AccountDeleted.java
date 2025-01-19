package com.devops.backend.user.domain.events;

import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
public class AccountDeleted implements UserEvent {
    UUID userId;
    String reason;
    LocalDateTime timestamp;
    UUID deletedById;
}
