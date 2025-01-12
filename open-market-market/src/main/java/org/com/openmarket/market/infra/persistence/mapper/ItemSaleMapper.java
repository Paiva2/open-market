package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class ItemSaleMapper {
    public static ItemSale toDomain(ItemSaleEntity entity) {
        if (entity == null) return null;

        ItemSale itemSale = new ItemSale();
        copyProperties(entity, itemSale);

        if (entity.getItem() != null) {
            Item item = new Item();
            copyProperties(entity.getItem(), item);
            itemSale.setItem(item);
        }

        if (entity.getUser() != null) {
            User user = new User();
            copyProperties(entity.getUser(), user);
            itemSale.setUser(user);
        }

        return itemSale;
    }

    public static ItemSaleEntity toPersistence(ItemSale entity) {
        if (entity == null) return null;

        ItemSaleEntity itemSale = new ItemSaleEntity();
        copyProperties(entity, itemSale);

        if (entity.getItem() != null) {
            ItemEntity item = new ItemEntity();
            copyProperties(entity.getItem(), item);
            itemSale.setItem(item);
        }

        if (entity.getUser() != null) {
            UserEntity user = new UserEntity();
            copyProperties(entity.getUser(), user);
            itemSale.setUser(user);
        }

        return itemSale;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
