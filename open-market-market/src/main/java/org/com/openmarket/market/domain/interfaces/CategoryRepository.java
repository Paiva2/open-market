package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.Category;

import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findByName(String name);

    Category save(Category category);
}
