package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfferRepository {
    List<Offer> getItemSaleOffers(UUID itemSaleId);

    void removeOffers(List<Offer> offers);

    PageableList<Offer> findAllByItemSale(UUID itemSaleId, int page, int size);

    Offer persist(Offer offer);

    Optional<Offer> findById(UUID id);

    void delete(UUID offerId);
}
