package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.com.openmarket.market.infra.persistence.entity.UserItemEntity;
import org.springframework.beans.BeanUtils;

public class UserItemMapper {
    public static UserItem toDomain(UserItemEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        UserItem userItem = new UserItem();
        copyProperties(persistenceEntity, userItem);

        if (persistenceEntity.getItem() != null) {
            Item item = new Item();
            copyProperties(persistenceEntity.getItem(), item);
            userItem.setItem(item);
        }

        if (persistenceEntity.getUser() != null) {
            User user = new User();
            copyProperties(persistenceEntity.getUser(), user);
            userItem.setUser(user);
        }

        if (persistenceEntity.getId() != null) {
            UserItem.KeyId id = new UserItem.KeyId();
            copyProperties(persistenceEntity.getId(), id);
            userItem.setId(id);
        }

        return userItem;
    }

    public static UserItemEntity toPersistence(UserItem persistenceEntity) {
        if (persistenceEntity == null) return null;
        UserItemEntity userItem = new UserItemEntity();
        copyProperties(persistenceEntity, userItem);

        if (persistenceEntity.getItem() != null) {
            ItemEntity item = new ItemEntity();
            copyProperties(persistenceEntity.getItem(), item);
            userItem.setItem(item);
        }

        if (persistenceEntity.getUser() != null) {
            UserEntity user = new UserEntity();
            copyProperties(persistenceEntity.getUser(), user);
            userItem.setUser(user);
        }

        if (persistenceEntity.getId() != null) {
            UserItemEntity.KeyId id = new UserItemEntity.KeyId();
            copyProperties(persistenceEntity.getId(), id);
            userItem.setId(id);
        }

        return userItem;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
