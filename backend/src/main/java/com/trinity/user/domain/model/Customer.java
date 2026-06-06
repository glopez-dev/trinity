package com.trinity.user.domain.model;

import java.time.Instant;

import com.trinity.common.domain.exception.BusinessRuleViolation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Domain aggregate for a customer, optionally linked to a Stripe OAuth account.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Customer extends User {

    private String stripeUserId;
    private Instant tokenExpiresAt;
    private String stripeAccessToken;
    private String stripeRefreshToken;

    @Builder.Default
    private UserType type = UserType.CUSTOMER;

    public boolean isTokenExpired() {
        return this.tokenExpiresAt != null && Instant.now().isAfter(this.tokenExpiresAt);
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
