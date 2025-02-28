package org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserItemInput {
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
