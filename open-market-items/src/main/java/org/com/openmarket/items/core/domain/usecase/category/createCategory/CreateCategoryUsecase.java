package org.com.openmarket.items.core.domain.usecase.category.createCategory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageType;
import org.com.openmarket.items.core.domain.interfaces.repository.CategoryRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.MessageRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.dto.CreateCategoryOutput;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.exception.CategoryAlreadyExists;
import org.com.openmarket.items.core.domain.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.com.openmarket.items.application.config.constants.QueueConstants.Market.MARKET_QUEUE;

@Service
@AllArgsConstructor
public class CreateCategoryUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final MessageRepository messageRepository;

    public CreateCategoryOutput execute(Long externalId, String categoryName) {
        checkUserExists(externalId);
        checkCategoryNameAlreadyExists(categoryName);

        Category category = fillCategory(categoryName);
        category = persistCategory(category);

        emitCreationToOtherServices(category);

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

    private void emitCreationToOtherServices(Category category) {
        try {
            CommonMessageDTO message = CommonMessageDTO.builder()
                .type(EnumMessageType.CREATED)
                .event(EnumMessageEvent.CATEGORY_EVENT)
                .data(category.getName())
                .build();

            messageRepository.sendMessage(MARKET_QUEUE, mapper.writeValueAsString(message));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CreateCategoryOutput mountOutput(Category category) {
        return CreateCategoryOutput.toOutput(category);
    }
}
