package org.com.openmarket.items.core.domain.interfaces.repository;

import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.core.domain.usecase.common.dto.PageableList;

import java.util.Optional;
import java.util.UUID;

public interface UserItemRepository {
    Optional<UserItem> findUserItem(UUID userId, UUID itemId, UUID attributeId);

    UserItem save(UserItem userItem);

    PageableList<UserItem> findAllByUser(UUID userId, int page, int size);

    void remove(UserItem userItem);
}
