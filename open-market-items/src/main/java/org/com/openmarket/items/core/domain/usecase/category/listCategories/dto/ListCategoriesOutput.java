package org.com.openmarket.items.core.domain.usecase.category.listCategories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListCategoriesOutput {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean isLast;
    private List<CategoryOutput> categories;

    public static ListCategoriesOutput toOutput(Page<Category> categories) {
        return ListCategoriesOutput.builder()
            .page(categories.getNumber() + 1)
            .size(categories.getSize())
            .totalItems(categories.getTotalElements())
            .totalPages(categories.getTotalPages())
            .isLast(categories.isLast())
            .categories(categories.getContent().stream().map(CategoryOutput::new).toList())
            .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryOutput {
        private Long id;
        private String name;

        public CategoryOutput(Category category) {
            this.id = category.getId();
            this.name = category.getName();
        }
    }
}
