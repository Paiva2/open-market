package org.com.openmarket.market.domain.core.usecase.offer.listOffersMade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOffersMadeOutput {
    private UUID id;
    private BigDecimal value;
    private Date createdAt;
    private ItemSaleOutput itemSale;

    public ListOffersMadeOutput(Offer offer) {
        this.id = offer.getId();
        this.value = offer.getValue();
        this.createdAt = offer.getCreatedAt();
        this.itemSale = ItemSaleOutput.builder()
            .id(offer.getItemSale().getId())
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemSaleOutput {
        private UUID id;
    }
}
