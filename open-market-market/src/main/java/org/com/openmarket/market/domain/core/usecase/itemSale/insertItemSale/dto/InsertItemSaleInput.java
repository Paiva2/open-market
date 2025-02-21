package org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.enumeration.EnumTimeOnSale;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InsertItemSaleInput {
    private Long quantity;
    private BigDecimal value;
    private EnumTimeOnSale timeOnSale;
    private Boolean acceptOffers;
    private Boolean onlyOffers;
    private String externalItemId;
    private String externalAttributeId;
}
