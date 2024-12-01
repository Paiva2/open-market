package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.ItemCategory;

import java.util.List;

public interface ItemCategoryRepository {
    ItemCategory save(ItemCategory itemCategory);

    List<ItemCategory> saveAll(List<ItemCategory> itemCategories);
}
