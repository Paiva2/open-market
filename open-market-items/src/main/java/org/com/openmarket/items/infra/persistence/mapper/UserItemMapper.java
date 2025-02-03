package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.AttributeItem;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.infra.persistence.entity.AttributeItemEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.com.openmarket.items.infra.persistence.entity.UserItemEntity;
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

        if (persistenceEntity.getAttribute() != null) {
            AttributeItem attributeItem = new AttributeItem();
            copyProperties(persistenceEntity.getAttribute(), attributeItem);
            userItem.setAttribute(attributeItem);
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

        if (persistenceEntity.getAttribute() != null) {
            AttributeItemEntity attributeItem = new AttributeItemEntity();
            copyProperties(persistenceEntity.getAttribute(), attributeItem);
            userItem.setAttribute(attributeItem);
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
