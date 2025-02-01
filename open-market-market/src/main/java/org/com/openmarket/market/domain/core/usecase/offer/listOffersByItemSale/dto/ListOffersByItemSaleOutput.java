package org.com.openmarket.market.domain.core.usecase.offer.listOffersByItemSale.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;

import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListOffersByItemSaleOutput {
    private UUID id;
    private BigDecimal value;
    private Date createdAt;
    private UserOutput user;
    private List<OfferItemOutput> offerItems;

    public ListOffersByItemSaleOutput(Offer offer) {
        this.id = offer.getId();
        this.value = offer.getValue();
        this.createdAt = offer.getCreatedAt();
        this.user = UserOutput.builder()
            .id(offer.getUser().getId())
            .email(offer.getUser().getEmail())
            .userName(offer.getUser().getUserName())
            .build();

        if (Objects.nonNull(offer.getOfferUserItems()) && !offer.getOfferUserItems().isEmpty()) {
            this.offerItems = offer.getOfferUserItems().stream().map(offerUserItem -> OfferItemOutput.builder()
                .item(ItemOutput.builder()
                    .id(offerUserItem.getUserItem().getItem().getId())
                    .externalId(offerUserItem.getUserItem().getItem().getExternalId())
                    .name(offerUserItem.getUserItem().getItem().getName())
                    .description(offerUserItem.getUserItem().getItem().getDescription())
                    .photoUrl(offerUserItem.getUserItem().getItem().getPhotoUrl())
                    .unique(offerUserItem.getUserItem().getItem().getUnique())
                    .baseSellingPrice(offerUserItem.getUserItem().getItem().getBaseSellingPrice())
                    .active(offerUserItem.getUserItem().getItem().getActive())
                    .build()
                ).quantity(offerUserItem.getUserItem().getQuantity())
                .build()
            ).toList();
        } else {
            this.offerItems = new ArrayList<>();
        }
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
    public static class OfferItemOutput {
        private ItemOutput item;
        private Long quantity;
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
    }
}
