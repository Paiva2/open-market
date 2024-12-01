package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.Item;

import java.util.Optional;

public interface ItemRepository {
    Optional<Item> findByName(String name);

    Item save(Item item);
}
