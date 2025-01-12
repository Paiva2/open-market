package org.com.openmarket.items.core.domain.usecase.item.listItems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemCategory;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Builder
@Data
public class ListItemsOutput {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean isLastPage;
    private Filters filters;
    private List<ItemOutput> items;

    public static ListItemsOutput toOutput(Page<Item> items, ListItemsInput input) {
        return ListItemsOutput.builder()
            .page(items.getNumber() + 1)
            .size(items.getSize())
            .totalItems(items.getTotalElements())
            .totalPages(items.getTotalPages())
            .isLastPage(items.isLast())
            .filters(Filters.builder()
                .name(input.getName())
                .active(input.getActive())
                .category(input.getCategory())
                .build()
            ).items(items.stream().map(ItemOutput::new).toList())
            .build();
    }

    @AllArgsConstructor
    @Builder
    @Data
    private static class Filters {
        private String name;
        private Boolean active;
        private Long category;
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class ItemOutput {
        private UUID id;
        private String name;
        private String description;
        private String photoUrl;
        private Boolean unique;
        private Boolean active;
        private BigDecimal baseSellingPrice;
        private List<CategoriesOutput> categories;

        public ItemOutput(Item item) {
            this.id = item.getId();
            this.name = item.getName();
            this.description = item.getDescription();
            this.photoUrl = item.getPhotoUrl();
            this.unique = item.getUnique();
            this.active = item.getActive();
            this.baseSellingPrice = item.getBaseSellingPrice();
            this.categories = item.getItemCategories().stream().map(CategoriesOutput::new).toList();
        }
    }

    @AllArgsConstructor
    @Builder
    @Data
    public static class CategoriesOutput {
        private Long id;
        private String name;

        public CategoriesOutput(ItemCategory itemCategory) {
            this.id = itemCategory.getCategory().getId();
            this.name = itemCategory.getCategory().getName();
        }
    }
}
