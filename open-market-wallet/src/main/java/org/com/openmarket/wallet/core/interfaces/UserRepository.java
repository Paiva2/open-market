package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByEmail(String email);

    Optional<User> findByExternalId(String externalId);

    User save(User user);
}
