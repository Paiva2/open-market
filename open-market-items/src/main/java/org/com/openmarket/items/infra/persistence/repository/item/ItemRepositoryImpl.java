package org.com.openmarket.items.infra.persistence.repository.item;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.repository.ItemRepository;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.com.openmarket.items.infra.persistence.mapper.ItemMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemRepositoryOrm repository;

    @Override
    public Optional<Item> findByName(String name) {
        Optional<ItemEntity> itemFound = repository.findByName(name);
        if (itemFound.isEmpty()) return Optional.empty();
        return Optional.of(ItemMapper.toDomain(itemFound.get()));
    }

    @Override
    public Item save(Item item) {
        ItemEntity itemEntitySaved = repository.save(ItemMapper.toPersistence(item));
        return ItemMapper.toDomain(itemEntitySaved);
    }
}
