package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemCategory;
import org.com.openmarket.items.infra.persistence.entity.ItemCategoryEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemMapper {
    public static Item toDomain(ItemEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        Item item = new Item();
        copyProperties(persistenceEntity, item);

        if (persistenceEntity.getItemCategories() != null) {
            List<ItemCategory> itemCategories = new ArrayList<>();

            for (ItemCategoryEntity itemCategory : persistenceEntity.getItemCategories()) {
                ItemCategory itemCategoryDomain = ItemCategoryMapper.toDomain(itemCategory);
                itemCategories.add(itemCategoryDomain);
            }

            item.setItemCategories(itemCategories);
        }

        return item;
    }

    public static ItemEntity toPersistence(Item entity) {
        if (entity == null) return null;
        ItemEntity itemEntity = new ItemEntity();
        copyProperties(entity, itemEntity);

        if (entity.getItemCategories() != null) {
            List<ItemCategoryEntity> itemCategories = new ArrayList<>();

            for (ItemCategory itemCategory : entity.getItemCategories()) {
                ItemCategoryEntity itemCategoryEntity = ItemCategoryMapper.toPersistence(itemCategory);
                itemCategories.add(itemCategoryEntity);
            }

            itemEntity.setItemCategories(itemCategories);
        }

        return itemEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
