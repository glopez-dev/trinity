package com.trinity.auth.model;

import java.time.Instant;

import org.springframework.util.Assert;

import com.trinity.auth.constant.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customer")
@Data
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

    @Column(nullable = false)
    private Instant tokenExpiresAt; 

    @Column(nullable = false)
    private String paypalAccessToken;

    @Column(nullable = false)
    private String paypalRefreshToken;

    @Builder.Default 
    private UserRole role = UserRole.CUSTOMER;

    public boolean isTokenExpired() {
        boolean expirationDateIsDefined = this.getTokenExpiresAt() != null;
        boolean expirationDateIsPassed = Instant.now().isAfter(this.getTokenExpiresAt());

        return expirationDateIsDefined && expirationDateIsPassed; 
    }
    
    public void updatePayPalToken(String accessToken, String refreshToken, Long expiresIn) {
        Assert.notNull(accessToken, "Access token must not be null");
        Assert.notNull(refreshToken, "Refresh token must not be null");
        Assert.notNull(expiresIn, "Expiration time must not be null");

        this.setPaypalAccessToken(accessToken);
        this.setPaypalRefreshToken(refreshToken);
        this.setTokenExpiresAt(Instant.now().plusSeconds(expiresIn));
    }

}