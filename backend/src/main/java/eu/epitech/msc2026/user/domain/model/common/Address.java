package eu.epitech.msc2026.user.domain.model.common;

import java.util.Objects;
import org.jmolecules.ddd.annotation.ValueObject;

/**
 * Address value object representing a physical address.
 * This class is immutable and implements value equality.
 */
@ValueObject
public final class Address {
    private final String street;
    private final String city;
    private final String zipCode;
    private final String country;
    private final String state; 

    private Address(Builder builder) {
        // Validate required fields
        if (builder.street == null || builder.street.trim().isEmpty()) {
            throw new IllegalArgumentException("Street cannot be null or empty");
        }
        if (builder.city == null || builder.city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (builder.zipCode == null || builder.zipCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Zip code cannot be null or empty");
        }
        if (builder.country == null || builder.country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
        if (builder.state == null || builder.state.trim().isEmpty()) {
            throw new IllegalArgumentException("State cannot be null or empty");
        }

        // Assign values
        this.street = builder.street.trim();
        this.city = builder.city.trim();
        this.zipCode = builder.zipCode.trim();
        this.country = builder.country.trim();
        this.state = builder.state.trim(); 
    }

    // Getters
    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state; 
    }

    // Value equality
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Address other = (Address) o;
        return Objects.equals(street, other.street) &&
               Objects.equals(city, other.city) &&
               Objects.equals(zipCode, other.zipCode) &&
               Objects.equals(country, other.country) &&
               Objects.equals(state, other.state); 
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, city, zipCode, country, state); 
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s %s, %s", 
            street, city, state, zipCode, country); 
    }

    // Builder pattern for object construction
    public static class Builder {
        private String street;
        private String city;
        private String zipCode;
        private String country;
        private String state; 

        public Builder withStreet(String street) {
            this.street = street;
            return this;
        }

        public Builder withCity(String city) {
            this.city = city;
            return this;
        }

        public Builder withZipCode(String zipCode) {
            this.zipCode = zipCode;
            return this;
        }

        public Builder withCountry(String country) {
            this.country = country;
            return this;
        }

        public Builder withState(String state) {
            this.state = state;
            return this;
        }

        public Address build() {
            return new Address(this);
        }
    }

    /**
     * Creates a new instance of the {@link Builder} class.
     *
     * @return a new {@link Builder} instance.
     */
    public static Builder builder() {
        return new Builder();
    }
}
