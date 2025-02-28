package org.com.openmarket.items.core.domain.usecase.userItem.createUserItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserItemInput {
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
