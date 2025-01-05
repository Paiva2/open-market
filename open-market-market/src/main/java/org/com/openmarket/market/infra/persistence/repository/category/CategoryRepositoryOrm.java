package org.com.openmarket.market.infra.persistence.repository.category;

import org.com.openmarket.market.infra.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepositoryOrm extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);
}
