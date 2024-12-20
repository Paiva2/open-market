package org.com.openmarket.items.core.domain.interfaces.repository;

import org.com.openmarket.items.core.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findByExternalId(Long id);

    User save(User user);
}
