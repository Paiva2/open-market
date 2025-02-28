package org.com.openmarket.market.infra.persistence.repository.attributeItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.AttributeItem;
import org.com.openmarket.market.domain.interfaces.AttributeItemRepository;
import org.com.openmarket.market.infra.persistence.entity.AttributeItemEntity;
import org.com.openmarket.market.infra.persistence.mapper.AttributeItemMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AttributeItemRepositoryImpl implements AttributeItemRepository {
    private final AttributeItemRepositoryOrm repository;

    @Override
    public void removeAll(List<AttributeItem> attributeItemList) {
        repository.deleteAll(attributeItemList.stream().map(AttributeItemMapper::toPersistence).toList());
    }

    @Override
    public AttributeItem persist(AttributeItem attributeItem) {
        AttributeItemEntity attributeItemEntity = repository.save(AttributeItemMapper.toPersistence(attributeItem));
        return AttributeItemMapper.toDomain(attributeItemEntity);
    }
}
