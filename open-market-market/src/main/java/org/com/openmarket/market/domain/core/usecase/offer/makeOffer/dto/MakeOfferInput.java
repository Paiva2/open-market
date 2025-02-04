package org.com.openmarket.market.domain.core.usecase.offer.makeOffer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MakeOfferInput {
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
