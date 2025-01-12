package org.com.openmarket.market.infra.persistence.repository.itemSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.interfaces.ItemSaleRepository;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.mapper.ItemSaleMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ItemSaleRepositoryImpl implements ItemSaleRepository {
    private final ItemSaleRepositoryOrm repository;

    @Override
    public ItemSale save(ItemSale itemSale) {
        ItemSaleEntity itemSaleEntity = repository.save(ItemSaleMapper.toPersistence(itemSale));
        return ItemSaleMapper.toDomain(itemSaleEntity);
    }
}
