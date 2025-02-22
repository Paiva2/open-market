package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.BaseAttribute;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.springframework.beans.BeanUtils;

public class ItemMapper {
    public static Item toDomain(ItemEntity entity) {
        if (entity == null) return null;

        Item item = new Item();
        copyProperties(entity, item);

        if (entity.getBaseAttribute() != null) {
            BaseAttribute baseAttribute = new BaseAttribute();
            copyProperties(entity.getBaseAttribute(), baseAttribute);
            item.setBaseAttribute(baseAttribute);
        }

        return item;
    }

    public static ItemEntity toPersistence(Item entity) {
        if (entity == null) return null;

        ItemEntity item = new ItemEntity();
        copyProperties(entity, item);

        return item;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
