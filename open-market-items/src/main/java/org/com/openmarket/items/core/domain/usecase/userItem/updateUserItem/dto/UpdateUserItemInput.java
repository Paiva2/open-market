package org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserItemInput {
    private String externalUserId;
    private String externalItemId;
    private String externalAttributeId;
    private UserItemInput userItemInput;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserItemInput {
        private Long quantity;
    }
}
