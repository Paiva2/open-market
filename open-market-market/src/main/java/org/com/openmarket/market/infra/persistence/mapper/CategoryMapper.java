package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.infra.persistence.entity.CategoryEntity;
import org.springframework.beans.BeanUtils;

public class CategoryMapper {
    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;

        Category category = new Category();
        copyProperties(entity, category);

        return category;
    }

    public static CategoryEntity toPersistence(Category entity) {
        if (entity == null) return null;

        CategoryEntity category = new CategoryEntity();
        copyProperties(entity, category);

        return category;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
