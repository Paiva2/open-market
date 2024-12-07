package org.com.openmarket.items.core.domain.usecase.category.deleteCategory;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.interfaces.repository.CategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemCategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DeleteCategoryUsecase {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    public void execute(Long externalId, Long categoryId) {
        findUser(externalId);

        Category category = findCategory(categoryId);
        removeAllItemCategories(category.getId());
        removeCategory(category);
    }

    private void findUser(Long externalId) {
        Optional<User> user = userRepository.findByExternalId(externalId);

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    private void removeAllItemCategories(Long categoryId) {
        itemCategoryRepository.removeAllByCategory(categoryId);
    }

    private void removeCategory(Category category) {
        categoryRepository.delete(category.getId());
    }
}
