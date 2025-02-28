package org.com.openmarket.items.infra.persistence.repository.userItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.core.domain.interfaces.repository.UserItemRepository;
import org.com.openmarket.items.infra.persistence.entity.UserItemEntity;
import org.com.openmarket.items.infra.persistence.mapper.UserItemMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserItemRepositoryImpl implements UserItemRepository {
    private final UserItemRepositoryOrm repository;

    @Override
    public Optional<UserItem> findUserItem(UUID userId, UUID itemId, UUID attributeId) {
        Optional<UserItemEntity> userItemEntity = repository.findUserItem(userId, itemId, attributeId);
        if (userItemEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntity.get()));
    }

    @Override
    public UserItem save(UserItem userItem) {
        UserItemEntity userItemEntity = repository.save(UserItemMapper.toPersistence(userItem));
        return UserItemMapper.toDomain(userItemEntity);
    }

    @Override
    public void remove(UserItem userItem) {
        repository.remove(UserItemMapper.toPersistence(userItem).getId());
    }
}
