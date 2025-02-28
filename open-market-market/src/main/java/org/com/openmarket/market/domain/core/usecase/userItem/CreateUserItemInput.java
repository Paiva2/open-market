package org.com.openmarket.market.domain.core.usecase.userItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserItemInput {
    private String externalItemId;
    private String externalUserId;
    private AttributeItemInput attribute;
    private Long quantity;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeItemInput {
        private String externalAttributeId;
        private String attributes;
    }
}
