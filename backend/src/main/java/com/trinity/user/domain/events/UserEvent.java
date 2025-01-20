package com.trinity.user.domain.events;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserEvent {
    UUID getUserId();

    LocalDateTime getTimestamp();
}
