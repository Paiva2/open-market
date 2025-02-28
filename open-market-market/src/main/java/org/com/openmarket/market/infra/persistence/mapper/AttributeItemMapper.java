package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.AttributeItem;
import org.com.openmarket.market.infra.persistence.entity.AttributeItemEntity;
import org.springframework.beans.BeanUtils;

public class AttributeItemMapper {
    public static AttributeItem toDomain(AttributeItemEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        AttributeItem attributeItem = new AttributeItem();
        copyProperties(persistenceEntity, attributeItem);

        return attributeItem;
    }

    public static AttributeItemEntity toPersistence(AttributeItem entity) {
        if (entity == null) return null;
        AttributeItemEntity attributeItem = new AttributeItemEntity();
        copyProperties(entity, attributeItem);

        return attributeItem;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
