package org.com.openmarket.items.core.domain.interfaces.repository;

import org.com.openmarket.items.core.domain.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    List<Category> findCategoriesById(List<Long> categoriesIds);

    Page<Category> listAll(int page, int size, String name);

    Optional<Category> findByName(String name);

    Optional<Category> findById(Long id);

    Category save(Category category);

    void delete(Long id);
}
