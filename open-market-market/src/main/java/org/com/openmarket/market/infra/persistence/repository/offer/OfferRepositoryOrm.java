package org.com.openmarket.market.infra.persistence.repository.offer;

import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepositoryOrm extends JpaRepository<OfferEntity, UUID> {
    @Query("select off from OfferEntity off where off.itemSale.id = :itemSaleId")
    List<OfferEntity> findAllByItemSaleId(@Param("itemSaleId") UUID itemSaleId);

    @Query("""
            select ofr from OfferEntity ofr
            join fetch ofr.user usr
            left join fetch ofr.offerUserItems oui
            left join fetch oui.userItem ui
            where ofr.itemSale.id = :itemSaleId
        """)
    Page<OfferEntity> findAllByItemSale(@Param("itemSaleId") UUID itemSaleId, Pageable pageable);

    @Query("""
            select ofr from OfferEntity ofr
            join fetch ofr.user usr
            left join fetch ofr.offerUserItems oui
            left join fetch oui.userItem ui
            where ofr.itemSale.id = :itemSaleId
        """)
    List<OfferEntity> findAllByItemSale(@Param("itemSaleId") UUID itemSaleId);

    @Query("""
            select ofr from OfferEntity ofr
            join fetch ofr.itemSale isl
            where ofr.user.id = :userId
            and isl.expirationDate > current_timestamp
        """)
    Page<OfferEntity> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("select off from OfferEntity off " +
        "join fetch off.itemSale isl " +
        "join fetch off.user usr " +
        "join fetch isl.userItem usi " +
        "where off.id = :offerId " +
        "and isl.expirationDate > current_timestamp ")
    Optional<OfferEntity> findByIdWithDeps(@Param("offerId") UUID offerId);

    @Query("select off from OfferEntity off where off.itemSale.id = :itemSaleId and off.user.id = :userId")
    Optional<OfferEntity> findByItemSaleAndUser(@Param("itemSaleId") UUID itemSaleId, @Param("userId") UUID userId);

    @Modifying
    @Query("delete OfferEntity where id in :ids")
    void removeAll(@Param("ids") List<UUID> ids);

    @Modifying
    @Query("delete OfferEntity where id = :id")
    void removeById(@Param("id") UUID id);
}
