package org.com.openmarket.market.domain.core.usecase.offerUserItem.listOfferUserItemsByOffer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.OfferUserItem;
import org.com.openmarket.market.domain.core.entity.UserItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOffersUserItemByOfferOutput {
    private UUID id;
    private Long quantity;
    private Date createdAt;
    private UserItemOutput item;

    public ListOffersUserItemByOfferOutput(OfferUserItem offerUserItem) {
        UserItem userItem = offerUserItem.getUserItem();

        this.id = offerUserItem.getId();
        this.quantity = offerUserItem.getQuantity();
        this.createdAt = offerUserItem.getCreatedAt();
        this.item = UserItemOutput.builder()
            .user(UserOutput.builder()
                .id(userItem.getUser().getId())
                .externalId(userItem.getUser().getExternalId())
                .userName(userItem.getUser().getUserName())
                .build())
            .item(ItemOutput.builder()
                .id(userItem.getItem().getId())
                .externalId(userItem.getItem().getExternalId())
                .name(userItem.getItem().getName())
                .description(userItem.getItem().getDescription())
                .photoUrl(userItem.getItem().getPhotoUrl())
                .unique(userItem.getItem().getUnique())
                .baseSellingPrice(userItem.getItem().getBaseSellingPrice())
                .build())
            .attribute(AttributeItemOutput.builder()
                .id(userItem.getAttribute().getId())
                .externalId(userItem.getAttribute().getExternalId())
                .attributes(userItem.getAttribute().getAttributes())
                .build())
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItemOutput {
        private UserOutput user;
        private ItemOutput item;
        private AttributeItemOutput attribute;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserOutput {
        private UUID id;
        private String externalId;
        private String userName;
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
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeItemOutput {
        private UUID id;
        private String externalId;
        private String attributes;
    }
}
