package com.trinity.user.model;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.user.constant.UserStatus;
import com.trinity.user.constant.UserType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder // Allows child classes to use the builder pattern with this class attributes. 
@NoArgsConstructor // Hibernate needs a no-args constructor.
@AllArgsConstructor // Builder pattern requires all args constructor.
@MappedSuperclass // This class is not an entity, but its attributes are inherited by entities.
/**
 * Base abstract class for authentication entities.
 * Provides common fields and functionality for all auth types.
 */
public abstract class AbstractUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email()
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserType type;

    @Column()
    private Instant lastLoginAt;

    @Builder.Default
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    @Version
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