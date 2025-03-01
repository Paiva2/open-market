package org.com.openmarket.market.domain.core.usecase.itemSale.listMineItemsOnSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.itemSale.listMineItemsOnSale.dto.ListMineItemsOnSaleOutput;
import org.com.openmarket.market.domain.interfaces.ItemSaleRepository;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListMineItemsOnSaleUsecase {
    private final UserRepository userRepository;
    private final ItemSaleRepository itemSaleRepository;

    public PageableList<ListMineItemsOnSaleOutput> execute(String externalUserId, int page, int size) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        PageableList<ItemSale> itemsSales = findItemSales(user, page, size);

        return mountOutput(itemsSales);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private PageableList<ItemSale> findItemSales(User user, int page, int size) {
        return itemSaleRepository.findAllByUser(user.getId(), page, size);
    }

    private PageableList<ListMineItemsOnSaleOutput> mountOutput(PageableList<ItemSale> itemSales) {
        return new PageableList<>(
            itemSales.getPage(),
            itemSales.getSize(),
            itemSales.getTotalItems(),
            itemSales.getTotalPages(),
            itemSales.isLast(),
            itemSales.getData().stream().map(ListMineItemsOnSaleOutput::new).toList()
        );
    }
}
