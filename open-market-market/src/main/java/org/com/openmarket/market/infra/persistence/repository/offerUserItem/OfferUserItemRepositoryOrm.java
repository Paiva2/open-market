package org.com.openmarket.market.infra.persistence.repository.offerUserItem;

import org.com.openmarket.market.infra.persistence.entity.OfferUserItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OfferUserItemRepositoryOrm extends JpaRepository<OfferUserItemEntity, UUID> {
    @Modifying
    @Query("delete from OfferUserItemEntity oui where oui.offer.id in (:offerIds)")
    void removeAllByOfferIds(@Param("offerIds") List<UUID> offerIds);
}
