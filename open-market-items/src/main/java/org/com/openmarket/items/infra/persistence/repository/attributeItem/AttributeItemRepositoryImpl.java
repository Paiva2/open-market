package org.com.openmarket.items.infra.persistence.repository.attributeItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.AttributeItem;
import org.com.openmarket.items.core.domain.interfaces.repository.AttributeItemRepository;
import org.com.openmarket.items.infra.persistence.entity.AttributeItemEntity;
import org.com.openmarket.items.infra.persistence.mapper.AttributeItemMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AttributeItemRepositoryImpl implements AttributeItemRepository {
    private final AttributeItemRepositoryOrm attributeItemRepositoryOrm;

    @Override
    public AttributeItem save(AttributeItem attributeItem) {
        AttributeItemEntity entity = attributeItemRepositoryOrm.save(AttributeItemMapper.toPersistence(attributeItem));
        return AttributeItemMapper.toDomain(entity);
    }
}
