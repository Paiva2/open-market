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
public class ItemCategory {
    private KeyId id;
    private Item item;
    private Category category;
    private Date createdAt;
    private Date updatedAt;

    public ItemCategory(KeyId id, Item item, Category category) {
        this.id = id;
        this.item = item;
        this.category = category;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Data
    public static class KeyId {
        private UUID itemId;
        private Long categoryId;
    }
}
