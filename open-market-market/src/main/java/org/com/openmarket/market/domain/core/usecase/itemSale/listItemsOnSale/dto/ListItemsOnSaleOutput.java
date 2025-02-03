package org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ListItemsOnSaleOutput {
    private UUID id;
    private Long quantity;
    private BigDecimal value;
    private Boolean acceptOffers;
    private Boolean onlyOffers;
    private ItemOutput item;
    private UserOutput seller;

    public ListItemsOnSaleOutput(ItemSale itemSale) {
        UserItem userItem = itemSale.getUserItem();
        Item item = userItem.getItem();
        User user = userItem.getUser();
        AttributeItem attribute = userItem.getAttribute();

        this.id = itemSale.getId();
        this.quantity = itemSale.getQuantity();
        this.value = itemSale.getValue();
        this.acceptOffers = itemSale.getAcceptOffers();
        this.onlyOffers = itemSale.getOnlyOffers();

        this.item = ItemOutput.builder()
            .id(item.getId())
            .name(item.getName())
            .externalId(item.getExternalId())
            .description(item.getDescription())
            .photoUrl(item.getPhotoUrl())
            .baseSellingPrice(item.getBaseSellingPrice())
            .attribute(AttributeOutput.builder()
                .id(attribute.getId())
                .externalId(attribute.getExternalId())
                .attributes(attribute.getAttributes())
                .build()
            ).build();

        this.seller = UserOutput.builder()
            .id(user.getId())
            .userName(user.getUserName())
            .email(user.getEmail())
            .build();
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
        private BigDecimal baseSellingPrice;
        private AttributeOutput attribute;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserOutput {
        private UUID id;
        private String userName;
        private String email;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeOutput {
        private UUID id;
        private String externalId;
        private String attributes;
    }
}
