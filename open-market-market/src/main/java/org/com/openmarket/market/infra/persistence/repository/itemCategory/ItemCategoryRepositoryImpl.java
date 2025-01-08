package org.com.openmarket.market.infra.persistence.repository.itemCategory;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemCategory;
import org.com.openmarket.market.domain.interfaces.ItemCategoryRepository;
import org.com.openmarket.market.infra.persistence.entity.ItemCategoryEntity;
import org.com.openmarket.market.infra.persistence.mapper.ItemCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ItemCategoryRepositoryImpl implements ItemCategoryRepository {
    private final ItemCategoryRepositoryOrm repository;

    @Override
    public List<ItemCategory> saveAll(List<ItemCategory> itemCategoryList) {
        List<ItemCategoryEntity> itemCategoryEntities = itemCategoryList.stream().map(ItemCategoryMapper::toPersistence).toList();
        List<ItemCategoryEntity> entities = repository.saveAll(itemCategoryEntities);
        return entities.stream().map(ItemCategoryMapper::toDomain).toList();
    }
}
