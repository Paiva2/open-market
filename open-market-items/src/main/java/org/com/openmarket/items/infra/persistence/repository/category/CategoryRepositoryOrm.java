package org.com.openmarket.items.infra.persistence.repository.category;

import org.com.openmarket.items.infra.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositoryOrm extends JpaRepository<CategoryEntity, Long> {
}
