package org.com.openmarket.market.infra.persistence.repository.category;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.domain.interfaces.CategoryRepository;
import org.com.openmarket.market.infra.persistence.entity.CategoryEntity;
import org.com.openmarket.market.infra.persistence.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryRepositoryOrm repository;

    @Override
    public Optional<Category> findByName(String name) {
        Optional<CategoryEntity> categoryEntity = repository.findByName(name);
        if (categoryEntity.isEmpty()) return Optional.empty();
        return Optional.of(CategoryMapper.toDomain(categoryEntity.get()));
    }

    @Override
    public Optional<Category> findByExternalId(String externalId) {
        Optional<CategoryEntity> categoryEntity = repository.findByExternalId(externalId);
        if (categoryEntity.isEmpty()) return Optional.empty();
        return Optional.of(CategoryMapper.toDomain(categoryEntity.get()));
    }

    @Override
    public Category save(Category category) {
        CategoryEntity categoryEntity = repository.save(CategoryMapper.toPersistence(category));
        return CategoryMapper.toDomain(categoryEntity);
    }

    @Override
    public List<Category> findCategoriesByExternalId(List<String> externalIds) {
        List<CategoryEntity> categoryEntities = repository.findCategoriesByExternalId(externalIds);
        return categoryEntities.stream().map(CategoryMapper::toDomain).toList();
    }

    @Override
    public void removeById(Long id) {
        repository.removeById(id);
    }
}
