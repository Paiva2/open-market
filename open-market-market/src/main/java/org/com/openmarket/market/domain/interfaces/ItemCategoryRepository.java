package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.ItemCategory;

import java.util.List;

public interface ItemCategoryRepository {
    List<ItemCategory> saveAll(List<ItemCategory> itemCategoryList);
}
