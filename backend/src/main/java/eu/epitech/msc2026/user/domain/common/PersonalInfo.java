package eu.epitech.msc2026.user.domain.common;


import java.util.Objects;
import eu.epitech.msc2026.user.domain.exception.InvalidUserDataException;
import org.jmolecules.ddd.annotation.ValueObject;


/**
 * Value Object representing the personal information of a user.
 * This class is immutable to ensure data integrity.
 */
@ValueObject
public class PersonalInfo {
    private final String firstName;
    private final String lastName;
    private final String phoneNumber;

    // Private constructor to enforce using of()
    private PersonalInfo(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
    }

    /**
     * Creates a new instance of PersonalInfo after validating the data.
     *
     * @param firstName The user's first name
     * @param lastName The user's last name
     * @param phoneNumber The user's phone number
     * @return A new instance of PersonalInfo
     * @throws InvalidUserDataException if the data is invalid
     */
    public static PersonalInfo of(String firstName, String lastName, String phoneNumber) {

        if (firstName == null || lastName == null || phoneNumber == null) {
            throw new InvalidUserDataException("None of the fields can be null");
        }

        validateFirstName(firstName);
        validateLastName(lastName);
        
        String normalizedPhone = normalizePhoneNumber(phoneNumber);
        validateNormalizedPhoneNumber(normalizedPhone);

        return new PersonalInfo(
            firstName.trim(),
            lastName.trim(),
            normalizedPhone
        );
    }

    private static void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new InvalidUserDataException("First name is required");
        }
        if (firstName.trim().length() < 2) {
            throw new InvalidUserDataException("First name must be at least 2 characters long");
        }
        if (!firstName.matches("^[\\p{L} .'-]+$")) {
            throw new InvalidUserDataException("First name contains invalid characters");
        }
    }

    private static void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new InvalidUserDataException("Last name is required");
        }
        if (lastName.trim().length() < 2) {
            throw new InvalidUserDataException("Last name must be at least 2 characters long");
        }
        if (!lastName.matches("^[\\p{L} .'-]+$")) {
            throw new InvalidUserDataException("Last name contains invalid characters");
        }
    }


    private static String normalizePhoneNumber(String phoneNumber) {
        // Remove all non-numeric characters except +
        return phoneNumber.replaceAll("[^+0-9]", "");
    }

    /**
     * Validates a normalized phone number format.
    * The phone number should already be normalized (containing only '+' and digits)
    * before calling this method.
    * 
    * Valid format:
    * - Must start with '+' followed by a non-zero digit
    * - Must contain between 8 and 15 digits after the '+'
    * - Must not contain any separators or special characters
    * 
    * Examples of valid normalized numbers:
    * - +33612345678
    * - +14155552671
    * 
    * @param normalizedPhoneNumber The phone number to validate, already normalized
    * @throws InvalidUserDataException if the phone number is null, empty, or doesn't match the required format
    */
    private static void validateNormalizedPhoneNumber(String normalizedPhoneNumber) {
        if (normalizedPhoneNumber == null || normalizedPhoneNumber.trim().isEmpty()) {
            throw new InvalidUserDataException("Phone number is required");
        }
    
        if (!normalizedPhoneNumber.matches("^\\+[1-9]\\d{7,14}$")) {
            throw new InvalidUserDataException("Invalid phone number format");
        }
    }

    // Getters
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the formatted full name.
     *
     * @return The concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Creates a new instance with an updated first name.
     *
     * @param newFirstName The new first name
     * @return A new instance of PersonalInfo
     */
    public PersonalInfo withFirstName(String newFirstName) {
        return PersonalInfo.of(newFirstName, this.lastName, this.phoneNumber);
    }

    /**
     * Creates a new instance with an updated last name.
     *
     * @param newLastName The new last name
     * @return A new instance of PersonalInfo
     */
    public PersonalInfo withLastName(String newLastName) {
        return PersonalInfo.of(this.firstName, newLastName, this.phoneNumber);
    }

    /**
     * Creates a new instance with an updated phone number.
     *
     * @param newPhoneNumber The new phone number
     * @return A new instance of PersonalInfo
     */
    public PersonalInfo withPhoneNumber(String newPhoneNumber) {
        return PersonalInfo.of(this.firstName, this.lastName, newPhoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalInfo that = (PersonalInfo) o;
        return Objects.equals(firstName, that.firstName) &&
               Objects.equals(lastName, that.lastName) &&
               Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, phoneNumber);
    }

    @Override
    public String toString() {
        return String.format("PersonalInfo{firstName='%s', lastName='%s', phoneNumber='%s'}",
            firstName, lastName, phoneNumber);
    }
}
