package eu.epitech.msc2026.user.domain.model.user;

import java.util.Objects;
import java.util.UUID;

import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Value object representing a unique user identifier.
 */
@ValueObject
public final class UserId {
    private final UUID value;

    private UserId(UUID value) {
        this.value = Objects.requireNonNull(value, "User ID value cannot be null");
    }

    /**
     * Creates a new UserId with a random UUID
     */
    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    /**
     * Creates a UserId from an existing UUID
     */
    public static UserId of(UUID id) {
        return new UserId(id);
    }

    /**
     * Creates a UserId from a string representation of a UUID
     */
    public static UserId fromString(String id) {
        return new UserId(UUID.fromString(id));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserId)) return false;
        UserId userId = (UserId) o;
        return Objects.equals(value, userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}