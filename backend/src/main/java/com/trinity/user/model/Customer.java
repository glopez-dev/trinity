package com.trinity.user.model;

import java.time.Instant;

import lombok.*;

import com.trinity.common.domain.exception.BusinessRuleViolation;
import com.trinity.user.constant.UserType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@Data
@SuperBuilder // Allows this class to use the builder pattern with it's parent class attributes.
@NoArgsConstructor // Hibernate needs a no-args constructor.
@AllArgsConstructor // Builder pattern requires all args constructor.
@EqualsAndHashCode(callSuper = false)
/**
 * Entity representing a customer, optionally linked to a Stripe OAuth account.
 */
public class Customer extends AbstractUser {

    @Column(unique = true)
    private String stripeUserId;

    @Column()
    private Instant tokenExpiresAt;

    @Column()
    private String stripeAccessToken;

    @Column()
    private String stripeRefreshToken;

    @Builder.Default
    @Column(nullable = false)
    private UserType type = UserType.CUSTOMER;

    public boolean isTokenExpired() {
        boolean expirationDateIsDefined = this.getTokenExpiresAt() != null;
        boolean expirationDateIsPassed = Instant.now().isAfter(this.getTokenExpiresAt());

        return expirationDateIsDefined && expirationDateIsPassed;
    }

    public void updateStripeToken(String accessToken, String refreshToken, Long expiresIn) {
        if (accessToken == null) {
            throw new BusinessRuleViolation("Access token must not be null");
        }
        if (refreshToken == null) {
            throw new BusinessRuleViolation("Refresh token must not be null");
        }
        if (expiresIn == null) {
            throw new BusinessRuleViolation("Expiration time must not be null");
        }

        this.setStripeAccessToken(accessToken);
        this.setStripeRefreshToken(refreshToken);
        this.setTokenExpiresAt(Instant.now().plusSeconds(expiresIn));
    }

    @Override
    public UserType getType() {
        return this.type;
    }

}