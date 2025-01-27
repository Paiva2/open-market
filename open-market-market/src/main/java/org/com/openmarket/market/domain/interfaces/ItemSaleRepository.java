package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.ItemSale;

import java.util.Optional;
import java.util.UUID;

public interface ItemSaleRepository {
    ItemSale save(ItemSale itemSale);

    Optional<ItemSale> findById(UUID id);

    void remove(UUID id);
}
