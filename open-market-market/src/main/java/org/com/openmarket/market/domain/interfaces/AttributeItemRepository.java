package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.AttributeItem;

import java.util.List;

public interface AttributeItemRepository {
    void removeAll(List<AttributeItem> attributeItemList);

    AttributeItem persist(AttributeItem attributeItem);
}
