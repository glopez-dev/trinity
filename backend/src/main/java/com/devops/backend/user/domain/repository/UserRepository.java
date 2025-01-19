package com.devops.backend.user.domain.repository;

import com.devops.backend.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> findById(UUID id);
    Optional<User> findByEmail(String email);
    User save(User user);
    void delete(User user);
}
