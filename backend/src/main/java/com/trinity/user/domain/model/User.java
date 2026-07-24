package com.trinity.user.domain.model;

import java.time.Instant;
import java.util.UUID;

import com.trinity.common.domain.exception.BusinessRuleViolation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Pure domain aggregate root for an application user. Holds the common identity,
 * credentials and status state machine; concrete {@link Customer}/{@link Employee}
 * subclasses add their own fields. Carries no persistence concern — the JPA
 * mapping lives in an anemic mirror hierarchy under infrastructure.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class User {

    private UUID id;
    private String email;
    private String hashedPassword;
    private String firstName;
    private String lastName;
    private UserType type;
    private Instant lastLoginAt;

    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private Instant createdAt;
    private Instant updatedAt;
    private Long version;

    public abstract UserType getType();

    /* User status management */

    public void updateLastLogin() {
        this.lastLoginAt = Instant.now();
    }

    /* Status state machine — transitions are validated against the current status. */

    public void activate() {
        ensureNotDeleted("activate");
        this.status = UserStatus.ACTIVE;
    }

    public void deactivate() {
        ensureNotDeleted("deactivate");
        this.status = UserStatus.INACTIVE;
    }

    public void lock() {
        ensureNotDeleted("lock");
        this.status = UserStatus.LOCKED;
    }

    public void markExpired() {
        ensureNotDeleted("expire");
        this.status = UserStatus.EXPIRED;
    }

    public void markDeleted() {
        this.status = UserStatus.DELETED;
    }

    private void ensureNotDeleted(String transition) {
        if (this.status == UserStatus.DELETED) {
            throw new BusinessRuleViolation(
                    "Cannot %s a deleted user".formatted(transition));
        }
    }

    public boolean accountIsLocked() {
        return status == UserStatus.LOCKED;
    }

    public boolean accountIsActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean accountIsInactive() {
        return status == UserStatus.INACTIVE;
    }

    public boolean accountIsExpired() {
        return status == UserStatus.EXPIRED;
    }
}
