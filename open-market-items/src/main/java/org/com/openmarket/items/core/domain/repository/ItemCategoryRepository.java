package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.ItemCategory;

import java.util.List;
import java.util.UUID;

public interface ItemCategoryRepository {
    ItemCategory save(ItemCategory itemCategory);

    List<ItemCategory> saveAll(List<ItemCategory> itemCategories);

    List<ItemCategory> findAllByItem(UUID itemId);

    void removeAllByItem(UUID itemId);

    void removeAllByCategory(Long categoryId);
}
