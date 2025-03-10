package org.com.openmarket.market.domain.core.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserItemMessageInput {
    private String externalUserId;
    private String externalItemId;
    private String externalAttributeId;
    private UserItemInput userItemInput;

    @Data
    @NoArgsConstructor
    public static class UserItemInput {
        private Long quantity;
        private String userId; // external user id that will or is owner of this user item

        public UserItemInput(Long quantity, String userId) {
            this.quantity = quantity;
            this.userId = userId;
        }

        public UserItemInput(Long quantity) {
            this.quantity = quantity;
        }
    }
}
