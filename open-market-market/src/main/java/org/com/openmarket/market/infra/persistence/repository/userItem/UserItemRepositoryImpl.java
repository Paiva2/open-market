package org.com.openmarket.market.infra.persistence.repository.userItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.domain.interfaces.UserItemRepository;
import org.com.openmarket.market.infra.persistence.entity.UserItemEntity;
import org.com.openmarket.market.infra.persistence.mapper.UserItemMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserItemRepositoryImpl implements UserItemRepository {
    private final UserItemRepositoryOrm repository;

    @Override
    public Optional<UserItem> getUserItem(UUID userId, UUID itemId) {
        Optional<UserItemEntity> userItemEntity = repository.findUserItem(userId, itemId);
        if (userItemEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntity.get()));
    }

    @Override
    public UserItem persist(UserItem userItem) {
        UserItemEntity userItemEntity = repository.save(UserItemMapper.toPersistence(userItem));
        return UserItemMapper.toDomain(userItemEntity);
    }
}
