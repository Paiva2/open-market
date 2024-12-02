package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemAlteration;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.infra.persistence.entity.ItemAlterationEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class ItemAlterationMapper {
    public static ItemAlteration toDomain(ItemAlterationEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        ItemAlteration itemAlteration = new ItemAlteration();
        copyProperties(persistenceEntity, itemAlteration);

        if (persistenceEntity.getItem() != null) {
            Item item = new Item();
            copyProperties(persistenceEntity.getItem(), item);
            itemAlteration.setItem(item);
        }

        if (persistenceEntity.getUser() != null) {
            User user = new User();
            copyProperties(persistenceEntity.getUser(), user);
            itemAlteration.setUser(user);
        }

        return itemAlteration;
    }

    public static ItemAlterationEntity toPersistence(ItemAlteration entity) {
        if (entity == null) return null;
        ItemAlterationEntity persistenceEntity = new ItemAlterationEntity();
        copyProperties(entity, persistenceEntity);

        if (entity.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            copyProperties(entity.getUser(), userEntity);
            persistenceEntity.setUser(userEntity);
        }

        if (entity.getItem() != null) {
            ItemEntity itemEntity = new ItemEntity();
            copyProperties(entity.getItem(), itemEntity);
            persistenceEntity.setItem(itemEntity);
        }

        return persistenceEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
