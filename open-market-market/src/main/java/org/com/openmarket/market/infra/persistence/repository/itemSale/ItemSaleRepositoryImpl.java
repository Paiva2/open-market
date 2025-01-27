package org.com.openmarket.market.infra.persistence.repository.itemSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.interfaces.ItemSaleRepository;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.mapper.ItemSaleMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ItemSaleRepositoryImpl implements ItemSaleRepository {
    private final ItemSaleRepositoryOrm repository;

    @Override
    public ItemSale save(ItemSale itemSale) {
        ItemSaleEntity itemSaleEntity = repository.save(ItemSaleMapper.toPersistence(itemSale));
        return ItemSaleMapper.toDomain(itemSaleEntity);
    }

    @Override
    public Optional<ItemSale> findById(UUID id) {
        Optional<ItemSaleEntity> itemSale = repository.findById(id);
        if (itemSale.isEmpty()) return Optional.empty();
        return Optional.of(ItemSaleMapper.toDomain(itemSale.get()));
    }

    @Override
    public void remove(UUID id) {
        repository.deleteById(id);
    }
}
