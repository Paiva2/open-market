package org.com.openmarket.items.infra.persistence.repository.item;

import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ItemRepositoryOrm extends JpaRepository<ItemEntity, UUID> {
    Optional<ItemEntity> findByName(String name);

    @Query(value = """
        select * from tb_items itm
        join tb_items_categories itc on itc.ict_item_id = itm.itm_id
        where (cast(:active as boolean) is null or itm.itm_active = :active)
        and (cast(:name as text) is null or itm.itm_name ilike concat('%', :name, '%'))
        and (cast(:unique as boolean) is null or itm.itm_unique = :unique)
        and (:maxPrice is null or itm.itm_base_selling_price <= cast(:maxPrice as numeric(12, 2)))
        and (:minPrice is null or itm.itm_base_selling_price >= cast(:minPrice as numeric(12, 2)))
        and (cast(:categoryId as int) is null or itc.ict_category_id = :categoryId)
        """, nativeQuery = true)
    Page<ItemEntity> findAllItems(@Param("name") String name, @Param("categoryId") Long categoryId, @Param("active") Boolean active, @Param("unique") Boolean unique, @Param("maxPrice") BigDecimal maxPrice, @Param("minPrice") BigDecimal minPrice, Pageable pageable);
}
