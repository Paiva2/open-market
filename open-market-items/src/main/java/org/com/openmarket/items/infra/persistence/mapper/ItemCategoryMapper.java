package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemCategory;
import org.com.openmarket.items.infra.persistence.entity.CategoryEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemCategoryEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.springframework.beans.BeanUtils;

public class ItemCategoryMapper {
    public static ItemCategory toDomain(ItemCategoryEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        ItemCategory itemCategory = new ItemCategory();
        copyProperties(persistenceEntity, itemCategory);

        if (persistenceEntity.getItem() != null) {
            Item item = new Item();
            copyProperties(persistenceEntity.getItem(), item);
            itemCategory.setItem(item);
        }

        if (persistenceEntity.getCategory() != null) {
            Category category = new Category();
            copyProperties(persistenceEntity.getCategory(), category);
            itemCategory.setCategory(category);
        }

        if (persistenceEntity.getId() != null) {
            ItemCategory.KeyId id = new ItemCategory.KeyId();
            copyProperties(persistenceEntity.getId(), id);
            itemCategory.setId(id);
        }

        return itemCategory;
    }

    public static ItemCategoryEntity toPersistence(ItemCategory entity) {
        if (entity == null) return null;
        ItemCategoryEntity itemCategoryEntity = new ItemCategoryEntity();
        copyProperties(entity, itemCategoryEntity);

        if (entity.getItem() != null) {
            ItemEntity itemEntity = new ItemEntity();
            copyProperties(entity.getItem(), itemEntity);
            itemCategoryEntity.setItem(itemEntity);
        }

        if (entity.getCategory() != null) {
            CategoryEntity categoryEntity = new CategoryEntity();
            copyProperties(entity.getCategory(), categoryEntity);
            itemCategoryEntity.setCategory(categoryEntity);
        }

        if (entity.getId() != null) {
            ItemCategoryEntity.KeyId id = new ItemCategoryEntity.KeyId();
            copyProperties(entity.getId(), id);
            itemCategoryEntity.setId(id);
        }

        return itemCategoryEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
