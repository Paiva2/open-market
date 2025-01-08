package org.com.openmarket.market.infra.persistence.repository.category;

import org.com.openmarket.market.infra.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryOrm extends JpaRepository<CategoryEntity, Long> {
    Optional<CategoryEntity> findByName(String name);

    @Query("select cat from CategoryEntity cat where cat.externalId in :externalIds")
    List<CategoryEntity> findCategoriesByExternalId(@Param("externalIds") List<String> externalIds);
}
