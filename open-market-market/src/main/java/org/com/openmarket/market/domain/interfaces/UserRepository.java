package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByExternalId(String externalId);

    User save(User user);
}
