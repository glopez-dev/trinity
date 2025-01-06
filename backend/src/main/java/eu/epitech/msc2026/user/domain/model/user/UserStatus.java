package eu.epitech.msc2026.user.domain.model.user;

/**
 * Enumeration representing the possible states of a user account.
 */
public enum UserStatus {
    ACTIVE("Active user with full access"),
    INACTIVE("Deactivated user account");

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