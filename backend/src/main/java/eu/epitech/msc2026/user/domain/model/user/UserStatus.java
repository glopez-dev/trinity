package eu.epitech.msc2026.user.domain.model.user;

/**
 * Enumeration representing the possible states of a user account.
 */
public enum UserStatus {
    ACTIVE("Active user with full access"),
    PENDING("User pending email verification"),
    INACTIVE("Deactivated user account"),
    LOCKED("Temporarily locked account"),
    SUSPENDED("Administratively suspended account");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isAccessible() {
        return this == ACTIVE;
    }
}