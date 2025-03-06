package org.com.openmarket.market.infra.persistence.repository.itemSale;

import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ItemSaleRepositoryOrm extends JpaRepository<ItemSaleEntity, UUID> {
    @Query(value = """
        select distinct isl.* from tb_items_sales isl
        join tb_users_items uit on (uit.uit_user_id = isl.isl_user_id and uit.uit_item_id = isl.isl_item_id and uit.uit_item_attribute_id = isl.isl_item_attribute_id)
        join tb_attributes_item atr on atr.aui_id = uit.uit_item_attribute_id
        join tb_users usr on usr.usr_id = uit.uit_user_id
        join tb_items itm on itm.itm_id = uit.uit_item_id
        left join tb_base_attributes bat on bat.bat_item_id = itm.itm_id
        join (
            select distinct ict1.ict_category_id, ict1.ict_item_id from tb_items_categories ict1
        ) as ict on ict.ict_item_id = itm.itm_id
        join tb_categories cat on cat.cat_id = ict.ict_category_id
        where (cast(:itemName as varchar) is null or lower(itm.itm_name) like concat('%', lower(:itemName), '%'))
        and isl.isl_expiration_date > now()
        and (cast(:externalCategoryId as varchar) is null or cat.cat_external_id = :externalCategoryId)
        and (:min is null or isl.isl_value >= :min)
        and (:max is null or isl.isl_value <= :max)
        """, nativeQuery = true)
    Page<ItemSaleEntity> findAllPaginatedFiltered(@Param("itemName") String itemName, @Param("externalCategoryId") String externalCategoryId, @Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);

    @Query(value = """
        select distinct isl.* from tb_items_sales isl
        join tb_users_items uit on (uit.uit_user_id = isl.isl_user_id and uit.uit_item_id = isl.isl_item_id and uit.uit_item_attribute_id = isl.isl_item_attribute_id)
        join tb_attributes_item atr on atr.aui_id = uit.uit_item_attribute_id
        join tb_users usr on usr.usr_id = uit.uit_user_id
        join tb_items itm on itm.itm_id = uit.uit_item_id
        left join tb_base_attributes bat on itm.itm_id = bat.bat_item_id
        join (
            select distinct ict1.ict_category_id, ict1.ict_item_id from tb_items_categories ict1
        ) as ict on ict.ict_item_id = itm.itm_id
        join tb_categories cat on cat.cat_id = ict.ict_category_id
        where isl.isl_user_id = :userId
        and isl.isl_expiration_date > now()
        """, nativeQuery = true)
    Page<ItemSaleEntity> findAllActiveByUser(@Param("userId") UUID userId, @Param("page") Integer page, @Param("size") Integer size, Pageable pageable);


    @Query("select isl from ItemSaleEntity isl " +
        "join fetch isl.userItem ui " +
        "join fetch ui.user usr " +
        "join fetch ui.item itm " +
        "join fetch ui.attribute atb " +
        "where isl.id = :id " +
        "and isl.quantity > 0")
    Optional<ItemSaleEntity> findByIdWithDeps(@Param("id") UUID id);

    @Query("select isl from ItemSaleEntity isl " +
        "join fetch isl.userItem usi " +
        "left join fetch isl.offers off " +
        "left join fetch off.user usr " +
        "where isl.expirationDate < current_timestamp")
    List<ItemSaleEntity> findAllExpired();

    @Modifying
    @Query("delete ItemSaleEntity where id = :id")
    void removeById(@Param("id") UUID id);
}
