package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.BaseAttribute;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.infra.persistence.entity.BaseAttributeEntity;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.springframework.beans.BeanUtils;

public class BaseAttributeMapper {
    public static BaseAttribute toDomain(BaseAttributeEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        BaseAttribute baseAttribute = new BaseAttribute();
        copyProperties(persistenceEntity, baseAttribute);

        if (persistenceEntity.getItem() != null) {
            Item item = new Item();
            copyProperties(persistenceEntity.getItem(), item);
            baseAttribute.setItem(item);
        }

        return baseAttribute;
    }

    public static BaseAttributeEntity toPersistence(BaseAttribute entity) {
        if (entity == null) return null;
        BaseAttributeEntity baseAttribute = new BaseAttributeEntity();
        copyProperties(entity, baseAttribute);

        if (entity.getItem() != null) {
            ItemEntity item = new ItemEntity();
            copyProperties(entity.getItem(), item);
            baseAttribute.setItem(item);
        }

        return baseAttribute;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
