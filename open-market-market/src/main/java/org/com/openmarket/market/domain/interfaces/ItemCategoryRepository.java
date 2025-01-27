package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.ItemCategory;

import java.util.List;
import java.util.UUID;

public interface ItemCategoryRepository {
    List<ItemCategory> saveAll(List<ItemCategory> itemCategoryList);

    List<ItemCategory> findAllByItem(UUID itemId);

    void removeAllByItem(UUID itemId);

    void removeAllByCategory(Long categoryId);
}
