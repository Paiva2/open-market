package org.com.openmarket.market.domain.interfaces;

import java.util.List;
import java.util.UUID;

public interface OfferUserItemRepository {
    void removeOfferUserItemByOfferIds(List<UUID> offerIds);
}
