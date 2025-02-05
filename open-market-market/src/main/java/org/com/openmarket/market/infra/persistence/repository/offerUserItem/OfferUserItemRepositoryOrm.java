package org.com.openmarket.market.infra.persistence.repository.offerUserItem;

import org.com.openmarket.market.infra.persistence.entity.OfferUserItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferUserItemRepositoryOrm extends JpaRepository<OfferUserItemEntity, UUID> {
    @Modifying
    @Query("delete from OfferUserItemEntity oui where oui.offer.id in (:offerIds)")
    void removeAllByOfferIds(@Param("offerIds") List<UUID> offerIds);

    @Query("select oui from OfferUserItemEntity oui " +
        "join fetch oui.userItem usi " +
        "where usi.user.id = :userId " +
        "and usi.item.id = :itemId " +
        "and usi.attribute.id = :attributeId")
    Optional<OfferUserItemEntity> findByUserItemAndAttribute(@Param("userId") UUID userId, @Param("itemId") UUID itemId, @Param("attributeId") UUID attributeId);

    @Query("select oui from OfferUserItemEntity oui " +
        "join fetch oui.userItem ui " +
        "join fetch oui.offer ofr " +
        "where oui.offer.id = :offerId")
    List<OfferUserItemEntity> findAllByOfferIdWithUserItem(@Param("offerId") UUID offerId);
}
