package org.com.openmarket.items.infra.persistence.mapper;


import org.com.openmarket.items.core.domain.entity.AttributeItem;
import org.com.openmarket.items.infra.persistence.entity.AttributeItemEntity;
import org.springframework.beans.BeanUtils;

public class AttributeItemMapper {
    public static AttributeItem toDomain(AttributeItemEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        AttributeItem attributeItem = new AttributeItem();
        copyProperties(persistenceEntity, attributeItem);

        return attributeItem;
    }

    public static AttributeItemEntity toPersistence(AttributeItem domain) {
        if (domain == null) return null;
        AttributeItemEntity baseAttribute = new AttributeItemEntity();
        copyProperties(domain, baseAttribute);

        return baseAttribute;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
