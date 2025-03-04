package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserItem {
    private KeyId id;
    private AttributeItem attribute;
    private User user;
    private Item item;
    private Long quantity;
    private Date createdAt;
    private Date updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class KeyId {
        private UUID attributeId;
        private UUID userId;
        private UUID itemId;
    }
}
