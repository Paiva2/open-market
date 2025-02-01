package org.com.openmarket.market.infra.persistence.repository.itemSale;

import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ItemSaleRepositoryOrm extends JpaRepository<ItemSaleEntity, UUID> {
    @Query(value = """
        select * from tb_items_sales isl
        join tb_users_items uit on (uit.uit_user_id = isl.isl_user_id and uit.uit_item_id = isl.isl_item_id and uit.uit_item_attribute_id = isl.isl_attribute_item_id)
        join tb_attributes_item atr on atr.aui_id = uit.uit_item_attribute_id
        join tb_users usr on usr.usr_id = uit.uit_user_id
        join tb_items itm on itm.itm_id = uit.uit_item_id
        where (:itemName is null or lower(itm.itm_name) like concat('%', lower(:itemName), '%'))
        and isl.isl_expiration_date > now()
        and (:min is null or isl.isl_value >= :min)
        and (:max is null or isl.isl_value <= :max)
        """, nativeQuery = true)
    Page<ItemSaleEntity> findAllPaginatedFiltered(@Param("itemName") String itemName, @Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);

    @Query("select isl from ItemSaleEntity isl join fetch isl.userItem ui where isl.id = :id")
    Optional<ItemSaleEntity> findByIdWithDeps(@Param("id") UUID id);
}
