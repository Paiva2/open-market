package org.com.openmarket.market.infra.persistence.repository.itemSale;

import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.UUID;

public interface ItemSaleRepositoryOrm extends JpaRepository<ItemSaleEntity, UUID> {
    @Query(value = """
        select * from tb_items_sales isl
        join tb_items itm on isl.isl_item_id = itm.itm_id
        join tb_users usr on usr.usr_id = isl.isl_user_id
        where (:itemName is null or lower(itm.itm_name) like concat('%', lower(:itemName), '%'))
        and isl.isl_expiration_date > now()
        and (:min is null or isl.isl_value >= :min)
        and (:max is null or isl.isl_value <= :max)
        """, nativeQuery = true)
    Page<ItemSaleEntity> findAllPaginatedFiltered(@Param("itemName") String itemName, @Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);
}
