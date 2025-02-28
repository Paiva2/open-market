package org.com.openmarket.items.core.domain.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserItemMessageInput {
    private String externalItemId;
    private String externalUserId;
    private AttributeItemOutput attribute;
    private Long quantity;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeItemOutput {
        private String externalAttributeId;
        private String attributes;
    }
}
