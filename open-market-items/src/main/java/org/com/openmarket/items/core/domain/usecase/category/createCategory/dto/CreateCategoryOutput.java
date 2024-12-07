package org.com.openmarket.items.core.domain.usecase.category.createCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCategoryOutput {
    private Long id;
    private String name;

    public static CreateCategoryOutput toOutput(Category category) {
        return CreateCategoryOutput.builder()
            .id(category.getId())
            .name(category.getName())
            .build();
    }
}
