package org.com.openmarket.items.core.domain.usecase.userItem.listUserItems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.entity.AttributeItem;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.UserItem;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserItemOutput {
    private ItemOutput item;
    private AttributeItemOutput attributeItem;
    private Long quantity;
    private Date updatedAt;

    public UserItemOutput(UserItem userItem) {
        Item itemOutput = userItem.getItem();
        AttributeItem attributeItemOutput = userItem.getAttribute();

        this.item = ItemOutput.builder()
            .id(itemOutput.getId())
            .name(itemOutput.getName())
            .description(itemOutput.getDescription())
            .photoUrl(itemOutput.getPhotoUrl())
            .unique(itemOutput.getUnique())
            .stackable(itemOutput.getStackable())
            .baseSellingPrice(itemOutput.getBaseSellingPrice())
            .baseAttribute(ItemOutput.BaseAttributeOutput.builder()
                .id(itemOutput.getBaseAttribute().getId())
                .attributes(itemOutput.getBaseAttribute().getAttributes())
                .build()
            ).active(itemOutput.getActive())
            .build();

        this.attributeItem = AttributeItemOutput.builder()
            .id(attributeItemOutput.getId())
            .attributes(attributeItemOutput.getAttributes())
            .build();

        this.quantity = userItem.getQuantity();
        this.updatedAt = userItem.getUpdatedAt();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemOutput {
        private UUID id;
        private String name;
        private String description;
        private String photoUrl;
        private Boolean unique;
        private Boolean stackable;
        private BigDecimal baseSellingPrice;
        private BaseAttributeOutput baseAttribute;
        private Boolean active;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class BaseAttributeOutput {
            private UUID id;
            private String attributes;
        }
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
