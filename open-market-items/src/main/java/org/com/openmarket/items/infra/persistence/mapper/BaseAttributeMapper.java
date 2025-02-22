package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.BaseAttribute;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.infra.persistence.entity.BaseAttributeEntity;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
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

    public static BaseAttributeEntity toPersistence(BaseAttribute domain) {
        if (domain == null) return null;
        BaseAttributeEntity baseAttribute = new BaseAttributeEntity();
        copyProperties(domain, baseAttribute);

        if (domain.getItem() != null) {
            ItemEntity item = new ItemEntity();
            copyProperties(domain.getItem(), item);
            baseAttribute.setItem(item);
        }

        return baseAttribute;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
