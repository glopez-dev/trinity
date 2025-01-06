package eu.epitech.msc2026.user.domain.model.user;

import eu.epitech.msc2026.user.domain.model.common.PersonalInfo;

import java.time.LocalDateTime;
import java.util.Objects;

import org.jmolecules.ddd.annotation.AggregateRoot;

/**
 * Abstract base class for all user types in the system.
 * Implements core user functionality and attributes.
 */
@AggregateRoot
public abstract class User {
    private final UserId id;
    private PersonalInfo personalInfo;
    private String email;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime lastLoginAt;

    protected User(UserId id, 
                  PersonalInfo personalInfo, 
                  String email, 
                  UserStatus status) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        this.personalInfo = Objects.requireNonNull(personalInfo, "Personal info cannot be null");
        setEmail(email);
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = this.createdAt;
    }

    /**
     * Updates the user's personal information
     */
    public void updatePersonalInfo(PersonalInfo newPersonalInfo) {
        Objects.requireNonNull(newPersonalInfo, "Personal info cannot be null");
        this.personalInfo = newPersonalInfo;
        updateLastModified();
    }

    /**
     * Updates the user's email address
     */
    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = email.trim().toLowerCase();
        updateLastModified();
    }

    /**
     * Updates the user's status
     */
    public void setStatus(UserStatus newStatus) {
        Objects.requireNonNull(newStatus, "Status cannot be null");
        this.status = newStatus;
        updateLastModified();
    }

    /**
     * Records a user login
     */
    public void recordLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    /**
     * Deactivates the user account
     */
    public void deactivate() {
        this.status = UserStatus.INACTIVE;
        updateLastModified();
    }

    private void updateLastModified() {
        this.lastModifiedAt = LocalDateTime.now();
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public String getEmail() {
        return email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}