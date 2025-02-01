package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserItem {
    private KeyId id;
    private AttributeItem attribute;
    private User user;
    private Item item;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;

    private List<ItemSale> itemSales;
    private List<OfferUserItem> offerUserItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        private UUID attributeItemId;
        private UUID userId;
        private UUID itemId;
    }
}
