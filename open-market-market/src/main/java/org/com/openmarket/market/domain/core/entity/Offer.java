package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Offer {
    private UUID id;
    private BigDecimal value;
    private Date createdAt;
    private Date updatedAt;
    private User user;
    private ItemSale itemSale;

    private List<OfferUserItem> offerUserItems;
}
