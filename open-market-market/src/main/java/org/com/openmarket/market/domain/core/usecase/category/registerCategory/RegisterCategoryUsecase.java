package org.com.openmarket.market.domain.core.usecase.category.registerCategory;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.dto.RegisterCategoryInput;
import org.com.openmarket.market.domain.core.usecase.category.registerCategory.exception.CategoryAlreadyExistsException;
import org.com.openmarket.market.domain.interfaces.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterCategoryUsecase {
    private final CategoryRepository categoryRepository;

    public void execute(RegisterCategoryInput input) {
        checkCategoryExists(input);

        Category category = fillCategory(input);
        persistCategory(category);
    }

    private void checkCategoryExists(RegisterCategoryInput input) {
        Optional<Category> category = categoryRepository.findByName(input.getName());

        if (category.isPresent()) {
            throw new CategoryAlreadyExistsException(input.getName());
        }
    }

    private Category fillCategory(RegisterCategoryInput input) {
        return Category.builder()
            .name(input.getName())
            .build();
    }

    private void persistCategory(Category category) {
        categoryRepository.save(category);
    }
}
