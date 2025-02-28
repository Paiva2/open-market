package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.OfferUserItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferUserItemRepository {
    void removeOfferUserItemByOfferIds(List<UUID> offerIds);

    List<OfferUserItem> persistAll(List<OfferUserItem> offerUserItems);

    List<OfferUserItem> findAllByItemSale(UUID itemSaleId);

    Optional<OfferUserItem> findByUserItemAndAttribute(UUID userId, UUID itemId, UUID attributeId);

    List<OfferUserItem> findByOfferIdWithDeps(UUID offerId);

    void removeAll(List<OfferUserItem> offerUserItems);
}
