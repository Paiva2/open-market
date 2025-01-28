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
    private KeyId id;
    private User user;
    private Item item;
    private Offer offer;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private UUID userId;
        private UUID itemId;
        private UUID offerId;
    }
}
