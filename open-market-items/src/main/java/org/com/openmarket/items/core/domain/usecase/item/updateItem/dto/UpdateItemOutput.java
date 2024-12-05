package org.com.openmarket.items.core.domain.usecase.item.updateItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateItemOutput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private List<CategoryOutput> categories;

    public static UpdateItemOutput toOutput(Item item) {
        return builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .photoUrl(item.getPhotoUrl())
            .unique(item.getUnique())
            .baseSellingPrice(item.getBaseSellingPrice())
            .categories(item.getItemCategories().stream().map(CategoryOutput::new).toList())
            .build();
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class CategoryOutput {
        private Long id;
        private String name;

        public CategoryOutput(ItemCategory itemCategory) {
            this.id = itemCategory.getCategory().getId();
            this.name = itemCategory.getCategory().getName();
        }
    }
}
