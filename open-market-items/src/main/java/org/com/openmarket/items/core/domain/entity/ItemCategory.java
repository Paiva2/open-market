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
public class ItemCategory {
    private KeyId id;
    private Item item;
    private Category category;
    private Date createdAt;
    private Date updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class KeyId {
        private Long itemId;
        private Long categoryId;
    }
}
