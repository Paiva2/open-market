package org.com.openmarket.items.infra.persistence.repository.item;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.repository.ItemRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.InvalidFieldException;
import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.com.openmarket.items.infra.persistence.mapper.ItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

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
    public Optional<Item> findById(UUID id) {
        Optional<ItemEntity> itemFound = repository.findById(id);
        if (itemFound.isEmpty()) return Optional.empty();
        return Optional.of(ItemMapper.toDomain(itemFound.get()));
    }

    @Override
    public Item save(Item item) {
        ItemEntity itemEntitySaved = repository.save(ItemMapper.toPersistence(item));
        return ItemMapper.toDomain(itemEntitySaved);
    }

    @Override
    public Page<Item> findAllItems(int page, int size, String name, Long category, Boolean active, String direction) {
        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.valueOf(direction.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new InvalidFieldException("direction");
        }

        Pageable pageable = PageRequest.of(page - 1, size, sortDirection, "itm_name");
        Page<ItemEntity> itemsEntities = repository.findAllItems(name, category, active, pageable);

        return itemsEntities.map(ItemMapper::toDomain);
    }
}
