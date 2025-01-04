package eu.epitech.msc2026.user.domain.exception;

/**
 * Exception thrown when user data validation fails.
 * This is a domain exception that represents a business rule violation.
 */
public class InvalidUserDataException extends RuntimeException {

    /**
     * Creates a new InvalidUserDataException with a specific message.
     *
     * @param message The error message describing the validation failure
     */
    public InvalidUserDataException(String message) {
        super(message);
    }

    /**
     * Creates a new InvalidUserDataException with a message and a cause.
     *
     * @param message The error message describing the validation failure
     * @param cause The underlying cause of the validation failure
     */
    public InvalidUserDataException(String message, Throwable cause) {
        super(message, cause);
    }
}