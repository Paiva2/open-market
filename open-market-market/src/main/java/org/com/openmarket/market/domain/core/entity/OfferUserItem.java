package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OfferUserItem {
    private UUID id;
    private UserItem userItem;
    private Offer offer;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;
}
