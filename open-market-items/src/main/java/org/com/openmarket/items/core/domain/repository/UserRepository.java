package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    User save(User user);
}
