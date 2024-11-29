package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserItem {
    private KeyId id;
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
        private Long userId;
        private Long itemId;
    }
}
