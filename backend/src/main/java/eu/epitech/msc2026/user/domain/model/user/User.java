package eu.epitech.msc2026.user.domain.model.user;

import eu.epitech.msc2026.user.domain.model.common.PersonalInfo;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import org.jmolecules.ddd.annotation.AggregateRoot;

/**
 * Abstract base class for all user types in the system.
 * Implements core user functionality and attributes.
 */
@AggregateRoot
public abstract class User {
    private final UUID id;
    private String email;
    private String hashedPassword;
    private PersonalInfo personalInfo;
    private UserStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
    private LocalDateTime lastLoginAt;

    protected User(
        UUID id, 
        String email, 
        String hashedPassword,
        PersonalInfo personalInfo, 
        UserStatus status
    ) {
        this.id = Objects.requireNonNull(id, "User ID cannot be null");
        setEmail(email);
        this.hashedPassword = Objects.requireNonNull(hashedPassword, "Hashed password cannot be null");
        this.personalInfo = Objects.requireNonNull(personalInfo, "Personal info cannot be null");
        this.status = Objects.requireNonNull(status, "Status cannot be null");
        this.createdAt = LocalDateTime.now();
        this.lastModifiedAt = this.createdAt;
        this.lastLoginAt = null; 
    }

    /**
     * Updates the user's last login time
     */
    private void updateLastModified() {
        this.lastModifiedAt = LocalDateTime.now();
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
     * Updates the user's password
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        updateLastModified();
    }

    /**
     * Updates the user's personal information
     */
    public void setPersonalInfo(PersonalInfo newPersonalInfo) {
        Objects.requireNonNull(newPersonalInfo, "Personal info cannot be null");
        this.personalInfo = newPersonalInfo;
        updateLastModified();
    }

    /**
     * Updates the user's status
     */
    private void setStatus(UserStatus newStatus) {
        Objects.requireNonNull(newStatus, "Status cannot be null");
        this.status = newStatus;
        updateLastModified();
    }

    /**
     * Activates the user account
     */
    public void activate() {
        this.setStatus(UserStatus.ACTIVE);
    }

    /**
     * Deactivates the user account
     */
    public void deactivate() {
        this.setStatus(UserStatus.INACTIVE);
    }

    
    // Getters
    public UUID getId() {
        return id;
    }

    
    public String getEmail() {
        return email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Returns the user's personal information 
     */
    public PersonalInfo getPersonalInfo() {
        return personalInfo;
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