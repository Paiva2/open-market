package org.com.openmarket.items.infra.persistence.repository.category;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.repository.CategoryRepository;
import org.com.openmarket.items.infra.persistence.entity.CategoryEntity;
import org.com.openmarket.items.infra.persistence.mapper.CategoryMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class CategoryRepositoryImpl implements CategoryRepository {
    private final CategoryRepositoryOrm repository;

    @Override
    public List<Category> findCategoriesById(List<Long> categoriesIds) {
        List<CategoryEntity> categoryEntities = repository.findAllById(categoriesIds);
        return categoryEntities.stream().map(CategoryMapper::toDomain).toList();
    }
}
