package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.UserItem;

import java.util.Optional;
import java.util.UUID;

public interface UserItemRepository {
    Optional<UserItem> getUserItemWithExternalAttributeId(UUID userId, UUID itemId, String externalAttributeId);

    UserItem persist(UserItem userItem);
}
