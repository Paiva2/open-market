package org.com.openmarket.items.infra.persistence.repository.itemCategory;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.ItemCategory;
import org.com.openmarket.items.core.domain.repository.ItemCategoryRepository;
import org.com.openmarket.items.infra.persistence.entity.ItemCategoryEntity;
import org.com.openmarket.items.infra.persistence.mapper.ItemCategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Component
public class ItemCategoryRepositoryImpl implements ItemCategoryRepository {
    private final ItemCategoryRepositoryOrm repository;

    @Override
    public ItemCategory save(ItemCategory itemCategory) {
        ItemCategoryEntity itemCategoryEntity = repository.save(ItemCategoryMapper.toPersistence(itemCategory));
        return ItemCategoryMapper.toDomain(itemCategoryEntity);
    }

    @Override
    public List<ItemCategory> saveAll(List<ItemCategory> itemCategories) {
        List<ItemCategoryEntity> entitiesToSave = itemCategories.stream().map(ItemCategoryMapper::toPersistence).toList();
        List<ItemCategoryEntity> entitiesSaved = repository.saveAll(entitiesToSave);
        return entitiesSaved.stream().map(ItemCategoryMapper::toDomain).toList();
    }

    @Override
    public List<ItemCategory> findAllByItem(UUID itemId) {
        List<ItemCategoryEntity> itemCategoryEntities = repository.findAllByItemId(itemId);
        return itemCategoryEntities.stream().map(ItemCategoryMapper::toDomain).toList();
    }

    @Override
    public void removeAllByItem(UUID itemId) {
        repository.deleteAllByItemId(itemId);
    }

    @Override
    public void removeAllByCategory(Long categoryId) {
        repository.deleteAllByCategoryId(categoryId);
    }
}
