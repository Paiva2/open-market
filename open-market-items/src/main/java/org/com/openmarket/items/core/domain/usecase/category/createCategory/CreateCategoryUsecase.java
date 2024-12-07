package org.com.openmarket.items.core.domain.usecase.category.createCategory;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.interfaces.repository.CategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.dto.CreateCategoryOutput;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.exception.CategoryAlreadyExists;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CreateCategoryUsecase {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public CreateCategoryOutput execute(Long externalId, String categoryName) {
        checkUserExists(externalId);
        checkCategoryNameAlreadyExists(categoryName);

        Category category = fillCategory(categoryName);
        category = persistCategory(category);

        return mountOutput(category);
    }

    private void checkUserExists(Long externalId) {
        Optional<User> user = userRepository.findByExternalId(externalId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
    }

    private void checkCategoryNameAlreadyExists(String categoryName) {
        Optional<Category> category = categoryRepository.findByName(categoryName);

        if (category.isPresent()) {
            throw new CategoryAlreadyExists(categoryName);
        }
    }

    private Category fillCategory(String categoryName) {
        return Category.builder()
            .name(categoryName)
            .build();
    }

    private Category persistCategory(Category category) {
        return categoryRepository.save(category);
    }

    private CreateCategoryOutput mountOutput(Category category) {
        return CreateCategoryOutput.toOutput(category);
    }
}
