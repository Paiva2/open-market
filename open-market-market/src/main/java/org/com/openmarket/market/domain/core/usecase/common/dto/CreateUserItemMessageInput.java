package org.com.openmarket.market.domain.core.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserItemMessageInput {
    private String externalUserId;
    private String externalItemId;
    private UserItemInput userItemInput;
    private UserItemAttributeInput itemAttribute;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItemInput {
        private Long quantity;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItemAttributeInput {
        private String attribute;
    }
}
