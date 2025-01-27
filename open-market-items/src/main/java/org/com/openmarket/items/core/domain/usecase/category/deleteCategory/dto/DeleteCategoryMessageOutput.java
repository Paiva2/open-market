package org.com.openmarket.items.core.domain.usecase.category.deleteCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCategoryMessageOutput {
    private String id;
}
