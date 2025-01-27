package org.com.openmarket.market.domain.core.usecase.category.deleteCategory;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.domain.core.usecase.category.deleteCategory.dto.DeleteCategoryInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.market.domain.interfaces.CategoryRepository;
import org.com.openmarket.market.domain.interfaces.ItemCategoryRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteCategoryUsecase {
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    @Transactional
    public void execute(DeleteCategoryInput input) {
        Category category = findCategory(input.getId());
        removeAllItemCategories(category);
        removeCategory(category);
    }

    private Category findCategory(String externalId) {
        return categoryRepository.findByExternalId(externalId).orElseThrow(CategoryNotFoundException::new);
    }

    private void removeAllItemCategories(Category category) {
        itemCategoryRepository.removeAllByCategory(category.getId());
    }

    private void removeCategory(Category category) {
        categoryRepository.removeById(category.getId());
    }
}
