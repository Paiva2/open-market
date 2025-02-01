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
public class ItemSale {
    private UUID id;
    private Long quantity;
    private BigDecimal value;
    private Date expirationDate;
    private Boolean acceptOffers;
    private Boolean onlyOffers;
    private Date createdAt;
    private Date updatedAt;

    private UserItem userItem;
    private List<Offer> offers;
}
