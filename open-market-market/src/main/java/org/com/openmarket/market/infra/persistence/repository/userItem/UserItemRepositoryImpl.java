package org.com.openmarket.market.infra.persistence.repository.userItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.domain.interfaces.UserItemRepository;
import org.com.openmarket.market.infra.persistence.entity.UserItemEntity;
import org.com.openmarket.market.infra.persistence.mapper.UserItemMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UserItemRepositoryImpl implements UserItemRepository {
    private final UserItemRepositoryOrm repository;

    @Override
    public Optional<UserItem> getUserItemWithExternalAttributeId(UUID userId, UUID itemId, String externalAttributeId) {
        Optional<UserItemEntity> userItemEntity = repository.findUserItemWithExternalAttribute(userId, itemId, externalAttributeId);
        if (userItemEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntity.get()));
    }

    @Override
    public Optional<UserItem> findByUserAndItemExternalId(UUID userId, String externalItemId) {
        Optional<UserItemEntity> userItemEntity = repository.findByUserAndExternalItemId(userId, externalItemId);
        if (userItemEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntity.get()));
    }

    @Override
    public UserItem persist(UserItem userItem) {
        UserItemEntity userItemEntity = repository.save(UserItemMapper.toPersistence(userItem));
        return UserItemMapper.toDomain(userItemEntity);
    }

    @Override
    public Optional<UserItem> findUserItemWithQuantity(UUID userId, String externalItemId, String externalAttributeId) {
        Optional<UserItemEntity> userItemEntities = repository.findByUserAndItemIdAndAttributeIdWithQuantity(userId, externalItemId, externalAttributeId);
        if (userItemEntities.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntities.get()));
    }

    @Override
    public List<UserItem> persistAll(List<UserItem> userItems) {
        List<UserItemEntity> userItemEntities = userItems.stream().map(UserItemMapper::toPersistence).toList();
        List<UserItemEntity> userItemsSaved = repository.saveAll(userItemEntities);
        return userItemsSaved.stream().map(UserItemMapper::toDomain).toList();
    }

    @Override
    public Optional<UserItem> findByItemSaleId(UUID itemSaleId) {
        Optional<UserItemEntity> userItemEntity = repository.findByItemSale(itemSaleId);
        if (userItemEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserItemMapper.toDomain(userItemEntity.get()));
    }

    @Override
    public void removeAll(List<UserItem> userItems) {
        repository.removeAll(userItems.stream().map(UserItemMapper::toPersistence).map(UserItemEntity::getId).toList());
    }

    @Override
    public void remove(UserItem userItem) {
        repository.remove(UserItemMapper.toPersistence(userItem).getId());
    }
}
