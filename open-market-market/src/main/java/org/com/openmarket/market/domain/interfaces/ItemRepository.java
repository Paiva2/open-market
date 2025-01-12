package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.Item;

import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findByName(String name);

    Optional<Item> findByExternalId(String externalId);

    Item save(Item item);
}
