package com.trinity.user.infrastructure.security;

import com.trinity.user.domain.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Spring Security adapter wrapping a domain {@link User}. Keeps the security
 * concern out of the domain model: the aggregate stays pure while this class
 * projects it onto the {@link UserDetails} contract.
 */
public class AppUserDetails implements UserDetails {

    private final User user;

    public AppUserDetails(User user) {
        this.user = user;
    }

    /** Exposes the wrapped domain user (e.g. for token claim generation). */
    public User getDomainUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getType().name()));
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getHashedPassword();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.accountIsExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.accountIsLocked();
    }

    @Override
    public boolean isEnabled() {
        return user.accountIsActive();
    }
}
