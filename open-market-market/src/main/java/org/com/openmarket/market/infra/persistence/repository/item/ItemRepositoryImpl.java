package org.com.openmarket.market.infra.persistence.repository.item;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.interfaces.ItemRepository;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.com.openmarket.market.infra.persistence.mapper.ItemMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemRepositoryOrm repository;

    @Override
    public Optional<Item> findByName(String name) {
        Optional<ItemEntity> itemEntity = repository.findByName(name);
        if (itemEntity.isEmpty()) return Optional.empty();
        return Optional.of(ItemMapper.toDomain(itemEntity.get()));
    }

    @Override
    public Item save(Item item) {
        ItemEntity itemEntity = repository.save(ItemMapper.toPersistence(item));
        return ItemMapper.toDomain(itemEntity);
    }
}
