package org.com.openmarket.items.core.domain.usecase.item.createItem.dto;

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
public class CreateItemOutput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private List<CategoryOutput> categories;
    private BaseAttributeOutput baseAttribute;

    public static CreateItemOutput toOutput(Item item) {
        return CreateItemOutput.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .photoUrl(item.getPhotoUrl())
            .unique(item.getUnique())
            .baseSellingPrice(item.getBaseSellingPrice())
            .baseAttribute(BaseAttributeOutput.builder()
                .id(item.getBaseAttribute().getId())
                .attributes(item.getBaseAttribute().getAttributes())
                .build())
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

    @Data
    @Builder
    @AllArgsConstructor
    public static class BaseAttributeOutput {
        private UUID id;
        private String attributes;
    }
}
