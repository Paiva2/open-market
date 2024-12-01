package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findCategoriesById(List<Long> categoriesIds);
}
