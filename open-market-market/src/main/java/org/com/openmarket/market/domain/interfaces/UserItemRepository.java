package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.UserItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserItemRepository {
    Optional<UserItem> getUserItemWithExternalAttributeId(UUID userId, UUID itemId, String externalAttributeId);

    Optional<UserItem> findByUserAndItemExternalId(UUID userId, String externalItemId);

    UserItem persist(UserItem userItem);

    Optional<UserItem> findUserItemWithQuantity(UUID userId, String externalItemId, String externalAttributeId);

    List<UserItem> persistAll(List<UserItem> userItems);

    Optional<UserItem> findByItemSaleId(UUID itemSaleId);

    void removeAll(List<UserItem> userItems);
}
