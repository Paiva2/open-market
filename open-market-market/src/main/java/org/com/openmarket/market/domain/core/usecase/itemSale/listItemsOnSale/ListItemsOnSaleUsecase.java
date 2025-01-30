package org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale.dto.ListItemsOnSaleOutput;
import org.com.openmarket.market.domain.interfaces.ItemSaleRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class ListItemsOnSaleUsecase {
    private final ItemSaleRepository itemSaleRepository;

    public PageableList<ListItemsOnSaleOutput> execute(int page, int size, String itemName, BigDecimal min, BigDecimal max) {
        if (page < 1) {
            page = 1;
        }

        if (size < 10) {
            size = 10;
        } else if (size > 50) {
            size = 50;
        }

        PageableList<ItemSale> itemSales = findItemSales(page, size, itemName, min, max);

        return new PageableList<>(
            itemSales.getPage(),
            itemSales.getSize(),
            itemSales.getTotalItems(),
            itemSales.getTotalPages(),
            itemSales.isLast(),
            itemSales.getData().stream().map(ListItemsOnSaleOutput::new).toList()
        );
    }

    private PageableList<ItemSale> findItemSales(int page, int size, String itemName, BigDecimal min, BigDecimal max) {
        return itemSaleRepository.findAllPaginated(page, size, itemName, min, max);
    }
}
