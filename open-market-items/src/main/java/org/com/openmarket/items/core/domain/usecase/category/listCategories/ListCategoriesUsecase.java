package org.com.openmarket.items.core.domain.usecase.category.listCategories;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.repository.CategoryRepository;
import org.com.openmarket.items.core.domain.usecase.category.listCategories.dto.ListCategoriesOutput;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListCategoriesUsecase {
    private final CategoryRepository categoryRepository;

    public ListCategoriesOutput execute(int page, int size, String name) {
        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        } else if (size > 50) {
            size = 50;
        }

        Page<Category> categories = findCategories(page, size, name);

        return ListCategoriesOutput.toOutput(categories);
    }

    private Page<Category> findCategories(int page, int size, String name) {
        return categoryRepository.listAll(page, size, name);
    }
}
