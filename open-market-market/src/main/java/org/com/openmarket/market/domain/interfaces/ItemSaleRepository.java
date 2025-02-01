package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ItemSaleRepository {
    ItemSale save(ItemSale itemSale);

    Optional<ItemSale> findByIdWithDeps(UUID id);

    void remove(UUID id);

    PageableList<ItemSale> findAllPaginated(int page, int size, String name, BigDecimal min, BigDecimal max);
}
