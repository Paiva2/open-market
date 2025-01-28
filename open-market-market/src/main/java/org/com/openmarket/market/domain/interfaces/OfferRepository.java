package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.Offer;

import java.util.List;
import java.util.UUID;

public interface OfferRepository {
    List<Offer> getItemSaleOffers(UUID itemSaleId);

    void removeOffers(List<Offer> offers);
}
