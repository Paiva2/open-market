package org.com.openmarket.market.domain.core.usecase.itemSale.listMineItemsOnSale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.UserItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListMineItemsOnSaleOutput {
    private UUID id;
    private Long quantity;
    private BigDecimal value;
    private Date expirationDate;
    private Boolean acceptOffers;
    private Boolean onlyOffers;
    private ItemOutput item;

    public ListMineItemsOnSaleOutput(ItemSale itemSale) {
        UserItem userItem = itemSale.getUserItem();

        this.id = itemSale.getId();
        this.quantity = itemSale.getQuantity();
        this.value = itemSale.getValue();
        this.expirationDate = itemSale.getExpirationDate();
        this.acceptOffers = itemSale.getAcceptOffers();
        this.onlyOffers = itemSale.getOnlyOffers();

        this.item = ItemOutput.builder()
            .id(userItem.getItem().getId())
            .externalId(userItem.getItem().getExternalId())
            .name(userItem.getItem().getName())
            .description(userItem.getItem().getDescription())
            .photoUrl(userItem.getItem().getPhotoUrl())
            .unique(userItem.getItem().getUnique())
            .baseSellingPrice(userItem.getItem().getBaseSellingPrice())
            .active(userItem.getItem().getActive())
            .stackable(userItem.getItem().getStackable())
            .baseAttribute(ItemOutput.BaseAttributeOutput.builder()
                .id(userItem.getItem().getBaseAttribute().getId())
                .attributes(userItem.getItem().getBaseAttribute().getAttributes())
                .build()
            ).attributeItem(ItemOutput.AttributeItemOutput.builder()
                .id(userItem.getAttribute().getId())
                .attributes(userItem.getAttribute().getAttributes())
                .build()
            ).build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOutput {
        private UUID id;
        private String externalId;
        private String name;
        private String description;
        private String photoUrl;
        private Boolean unique;
        private BigDecimal baseSellingPrice;
        private Boolean active;
        private Boolean stackable;
        private BaseAttributeOutput baseAttribute;
        private AttributeItemOutput attributeItem;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BaseAttributeOutput {
            private UUID id;
            private String attributes;
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class AttributeItemOutput {
            private UUID id;
            private String attributes;
        }
    }
}
