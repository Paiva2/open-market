package org.com.openmarket.items.core.domain.repository;

import org.com.openmarket.items.core.domain.entity.Item;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepository {
    Optional<Item> findByName(String name);

    Optional<Item> findById(UUID id);

    Item save(Item item);

    Page<Item> findAllItems(int page, int size, String name, Long category, Boolean active, String direction);
}
