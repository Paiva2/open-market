package org.com.openmarket.market.domain.core.usecase.offer.listOffersByItemSale;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.offer.listOffersByItemSale.dto.ListOffersByItemSaleOutput;
import org.com.openmarket.market.domain.interfaces.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ListOffersByItemSaleUsecase {
    private final OfferRepository offerRepository;

    public PageableList<ListOffersByItemSaleOutput> execute(UUID itemSaleId, int page, int size) {
        if (page < 1) {
            page = 0;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        PageableList<Offer> offer = findOffer(itemSaleId, page, size);

        return new PageableList<>(
            offer.getPage(),
            offer.getSize(),
            offer.getTotalItems(),
            offer.getTotalPages(),
            offer.isLast(),
            offer.getData().stream().map(ListOffersByItemSaleOutput::new).toList()
        );
    }

    private PageableList<Offer> findOffer(UUID itemSaleId, int page, int size) {
        return offerRepository.findAllByItemSale(itemSaleId, page, size);
    }
}
