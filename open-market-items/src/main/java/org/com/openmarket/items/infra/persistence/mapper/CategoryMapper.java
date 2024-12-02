package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.infra.persistence.entity.CategoryEntity;
import org.springframework.beans.BeanUtils;

public class CategoryMapper {
    public static Category toDomain(CategoryEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        Category category = new Category();
        copyProperties(persistenceEntity, category);

        return category;
    }

    public static CategoryEntity toPersistence(Category entity) {
        if (entity == null) return null;
        CategoryEntity categoryEntity = new CategoryEntity();
        copyProperties(entity, categoryEntity);

        return categoryEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
