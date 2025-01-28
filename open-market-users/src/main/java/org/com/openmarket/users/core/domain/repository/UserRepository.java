package org.com.openmarket.users.core.domain.repository;

import org.com.openmarket.users.core.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);
}
