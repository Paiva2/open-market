package org.com.openmarket.market.infra.persistence.repository.itemSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.interfaces.ItemSaleRepository;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.mapper.ItemSaleMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public Optional<ItemSale> findByIdWithDeps(UUID id) {
        Optional<ItemSaleEntity> itemSale = repository.findByIdWithDeps(id);
        if (itemSale.isEmpty()) return Optional.empty();
        return Optional.of(ItemSaleMapper.toDomain(itemSale.get()));
    }

    @Override
    public void remove(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public PageableList<ItemSale> findAllPaginated(int page, int size, String name, BigDecimal min, BigDecimal max) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "isl_created_at");
        Page<ItemSaleEntity> itemSales = repository.findAllPaginatedFiltered(name, min, max, pageable);

        return new PageableList<>(
            page + 1,
            size,
            itemSales.getTotalElements(),
            itemSales.getTotalPages(),
            itemSales.isLast(),
            itemSales.stream().map(ItemSaleMapper::toDomain).toList()
        );
    }
}
