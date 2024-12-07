package org.com.openmarket.items.infra.persistence.repository.category;

import org.com.openmarket.items.infra.persistence.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepositoryOrm extends JpaRepository<CategoryEntity, Long> {
    @Query(value = """
        select * from tb_categories cat
        where (:name is null or lower(cat.cat_name) like concat('%', lower(:name), '%'))
        order by cat.cat_name asc
        """, nativeQuery = true)
    Page<CategoryEntity> findAll(@Param("name") String name, Pageable pageable);
}
