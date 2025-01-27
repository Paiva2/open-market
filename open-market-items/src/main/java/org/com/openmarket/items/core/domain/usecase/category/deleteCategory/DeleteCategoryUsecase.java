package org.com.openmarket.items.core.domain.usecase.category.deleteCategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageType;
import org.com.openmarket.items.core.domain.interfaces.repository.CategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemCategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.MessageRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.category.deleteCategory.dto.DeleteCategoryMessageOutput;
import org.com.openmarket.items.core.domain.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.com.openmarket.items.application.config.constants.QueueConstants.Market.MARKET_QUEUE;

@Service
@AllArgsConstructor
public class DeleteCategoryUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MessageRepository messageRepository;

    public void execute(Long externalId, Long categoryId) {
        findUser(externalId);

        Category category = findCategory(categoryId);
        removeAllItemCategories(category.getId());
        removeCategory(category);
        sendMessage(category);
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

    private void sendMessage(Category category) {
        try {
            DeleteCategoryMessageOutput messageOutput = DeleteCategoryMessageOutput.builder()
                .id(category.getId().toString())
                .build();

            CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.DELETED)
                .event(EnumMessageEvent.CATEGORY_EVENT)
                .data(mapper.writeValueAsString(messageOutput))
                .build();

            messageRepository.sendMessage(MARKET_QUEUE, mapper.writeValueAsString(commonMessageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
