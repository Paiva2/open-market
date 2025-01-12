package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.UserItem;

import java.util.Optional;
import java.util.UUID;

public interface UserItemRepository {
    Optional<UserItem> getUserItem(UUID userId, UUID itemId);

    UserItem persist(UserItem userItem);
}
