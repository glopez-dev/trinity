package com.trinity.user.domain.repository;

import com.trinity.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    User save(User user);

    void delete(User user);
}
