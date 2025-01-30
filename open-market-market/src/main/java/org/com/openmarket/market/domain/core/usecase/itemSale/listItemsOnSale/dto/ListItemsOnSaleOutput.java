package org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.User;

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
        Item item = itemSale.getItem();
        User user = itemSale.getUser();

        this.id = itemSale.getId();
        this.quantity = itemSale.getQuantity();
        this.value = itemSale.getValue();
        this.acceptOffers = itemSale.getAcceptOffers();
        this.onlyOffers = itemSale.getOnlyOffers();
        
        this.item = ItemOutput.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .photoUrl(item.getPhotoUrl())
            .baseSellingPrice(item.getBaseSellingPrice())
            .build();

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
        private String name;
        private String description;
        private String photoUrl;
        private BigDecimal baseSellingPrice;
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
}
