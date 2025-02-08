package org.com.openmarket.market.domain.core.usecase.offer.editOffer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditOfferInput {
    private BigDecimal value;
    private List<UserItemInput> userItems;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItemInput {
        private String externalItemId;
        private String externalAttributeId;
        private Long quantity;
    }
}
