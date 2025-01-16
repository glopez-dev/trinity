package com.trinity.auth.model;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.trinity.auth.constant.UserRole;
import com.trinity.auth.constant.UserStatus;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder // Allows child classes to use the builder pattern with this class attributes. 
@NoArgsConstructor // Hibernate needs a no-args constructor.
@AllArgsConstructor // Builder pattern requires all args constructor.
@MappedSuperclass // This class is not an entity, but its attributes are inherited by entities.
/**
* Base abstract class for authentication entities.
* Provides common fields and functionality for all auth types.
*/
public abstract class AbstractUser implements UserDetails {
   
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Builder.Default
    @Column(nullable = false)
    private Instant lastLoginAt = Instant.now();

    @Builder.Default
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
    
    @Version
    private Long version;

    public abstract UserRole getRole();

    /* User status management */

    public void updateLastLogin() {
        this.lastLoginAt = Instant.now();
    }

    public void setStatusInactive() {
        this.status = UserStatus.INACTIVE;
    }

    public void setStatusActive() {
        this.status = UserStatus.ACTIVE;
    }


    public boolean accountIsLocked() {
        return status == UserStatus.LOCKED;
    }

    public boolean accountIsActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean accountIsInactive() {
        return status == UserStatus.INACTIVE;
    }

    public boolean accountIsExpired() {
        return status == UserStatus.EXPIRED;
    }

    /* Spring security UserDetails interface implementation */

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return hashedPassword;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !this.accountIsExpired(); 
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.accountIsLocked();
    }

    @Override
    public boolean isEnabled() {
        return true; 
    }
}