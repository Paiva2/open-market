package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CategoryRepository {
    List<Category> findCategoriesById(List<Long> categoriesIds);

    Page<Category> listAll(int page, int size, String name);
}
