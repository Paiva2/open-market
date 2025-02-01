package org.com.openmarket.market.infra.persistence.repository.offer;

import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OfferRepositoryOrm extends JpaRepository<OfferEntity, UUID> {
    @Query("select off from OfferEntity off where off.itemSale.id = :itemSaleId")
    List<OfferEntity> findAllByItemSaleId(@Param("itemSaleId") UUID itemSaleId);

    @Query("""
            select ofr from OfferEntity ofr
            join fetch ofr.user usr
            left join fetch ofr.offerUserItems oui
            join fetch oui.userItem.item itm
            where ofr.itemSale.id = :itemSaleId
        """)
    Page<OfferEntity> findAllByItemSale(@Param("itemSaleId") UUID itemSaleId, Pageable pageable);
}
