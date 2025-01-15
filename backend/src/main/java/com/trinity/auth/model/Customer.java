package com.trinity.auth.model;

import java.time.Instant;

import org.springframework.util.Assert;

import com.trinity.auth.constant.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@Getter
@SuperBuilder
@NoArgsConstructor // Hibernate needs a no-args constructor.
@EqualsAndHashCode(callSuper = false)
/**
 * Entity representing a customer authenticated through PayPal OAuth
 * @param expiresIn the number of seconds until the token expires
 */
public class Customer extends AbstractUser {

    @Column(unique = true, nullable = false)
    private String paypalUserId;

    private Instant tokenExpiresAt; 

    private String paypalAccessToken;

    private String paypalRefreshToken;

    @Builder.Default 
    private UserRole role = UserRole.CUSTOMER;

    public boolean isTokenExpired() {
        boolean expirationDateIsDefined = tokenExpiresAt != null;
        boolean expirationDateIsPassed = Instant.now().isAfter(tokenExpiresAt);

        return expirationDateIsDefined && expirationDateIsPassed; 
    }
    
    public void updatePayPalToken(String accessToken, String refreshToken, Long expiresIn) {
        Assert.notNull(accessToken, "Access token must not be null");
        Assert.notNull(refreshToken, "Refresh token must not be null");
        Assert.notNull(expiresIn, "Expiration time must not be null");

        this.paypalAccessToken = accessToken;
        this.paypalRefreshToken = refreshToken;
        this.tokenExpiresAt = Instant.now().plusSeconds(expiresIn);
    }

}