package org.com.openmarket.items.core.domain.usecase.item.createItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.application.gateway.message.dto.CommonMessageDTO;
import org.com.openmarket.items.core.domain.entity.Category;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.ItemCategory;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.enumeration.EnumItemAlteration;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageType;
import org.com.openmarket.items.core.domain.interfaces.repository.*;
import org.com.openmarket.items.core.domain.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.items.core.domain.usecase.common.exception.InvalidFieldException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemInput;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemMessageOutput;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemOutput;
import org.com.openmarket.items.core.domain.usecase.item.createItem.exception.ItemAlreadyExistsException;
import org.com.openmarket.items.core.domain.usecase.itemAlteration.registerItemAlteration.RegisterItemAlterationUsecase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.com.openmarket.items.application.config.constants.QueueConstants.Market.MARKET_QUEUE;

@AllArgsConstructor
@Service
public class CreateItemUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final MessageRepository messageRepository;
    private final RegisterItemAlterationUsecase registerItemAlterationUsecase;

    public CreateItemOutput execute(Long userId, CreateItemInput input) {
        validateRequiredFields(input);

        User user = findUser(userId);

        checkItemAlreadyExists(input.getName());
        List<Category> categories = findCategories(input.getCategoriesIds());

        Item item = fillItem(input);
        item = persistItem(item);

        List<ItemCategory> itemCategories = fillItemCategories(item, categories);
        itemCategories = persistItemCategories(itemCategories);

        item.setItemCategories(itemCategories);

        persistItemAlteration(user, item);

        sendItemCreation(item);
        return mountOutput(item);
    }

    private void validateRequiredFields(CreateItemInput input) {
        if (input.getBaseSellingPrice().compareTo(new BigDecimal("0")) < 0) {
            throw new InvalidFieldException("baseSellingPrice can't be less than 0");
        }

        if (input.getName() == null || input.getName().isBlank()) {
            throw new InvalidFieldException("name");
        }

        if (input.getDescription() == null || input.getDescription().isBlank()) {
            throw new InvalidFieldException("description");
        }

        if (input.getPhotoUrl() == null || input.getPhotoUrl().isBlank()) {
            throw new InvalidFieldException("photoUrl");
        }

        if (input.getCategoriesIds() == null || input.getCategoriesIds().isEmpty()) {
            throw new InvalidFieldException("categoriesIds can't be empty");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findByExternalId(userId).orElseThrow(UserNotFoundException::new);
    }

    private void checkItemAlreadyExists(String itemName) {
        Optional<Item> item = itemRepository.findByName(itemName);

        if (item.isPresent()) {
            throw new ItemAlreadyExistsException(itemName);
        }
    }

    private List<Category> findCategories(List<Long> categoriesIds) {
        List<Category> categories = categoryRepository.findCategoriesById(categoriesIds);

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Categories not found: " + categoriesIds);
        }

        if (categories.size() != categoriesIds.size()) {
            List<Long> categoriesFoundIds = categories.stream().map(Category::getId).toList();
            List<Long> categoriesNotFoundIds = categoriesIds.stream().filter(catId -> !categoriesFoundIds.contains(catId)).toList();

            if (!categoriesNotFoundIds.isEmpty()) {
                throw new CategoryNotFoundException("Categories not found: " + categoriesNotFoundIds);
            }
        }

        return categories;
    }

    private Item fillItem(CreateItemInput input) {
        return Item.builder()
            .name(input.getName())
            .description(input.getDescription())
            .photoUrl(input.getPhotoUrl())
            .unique(input.getUnique())
            .baseSellingPrice(input.getBaseSellingPrice())
            .active(true)
            .build();
    }

    private Item persistItem(Item item) {
        return itemRepository.save(item);
    }

    private List<ItemCategory> fillItemCategories(Item item, List<Category> categories) {
        List<ItemCategory> itemCategories = new ArrayList<>();

        for (Category category : categories) {
            ItemCategory.KeyId icKeyId = new ItemCategory.KeyId(item.getId(), category.getId());
            ItemCategory itemCategory = new ItemCategory(icKeyId, item, category);

            itemCategories.add(itemCategory);
        }

        return itemCategories;
    }

    private List<ItemCategory> persistItemCategories(List<ItemCategory> itemCategories) {
        return itemCategoryRepository.saveAll(itemCategories);
    }

    private void persistItemAlteration(User user, Item item) {
        registerItemAlterationUsecase.execute(user, item, EnumItemAlteration.CREATION);
    }

    private void sendItemCreation(Item item) {
        try {
            CreateItemMessageOutput messageOutput = CreateItemMessageOutput.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .photoUrl(item.getPhotoUrl())
                .unique(item.getUnique())
                .baseSellingPrice(item.getBaseSellingPrice())
                .active(item.getActive())
                .categoriesIds(item.getItemCategories().stream().map(ItemCategory::getCategory).map(Category::getId).toList())
                .build();
            
            CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.CREATED)
                .event(EnumMessageEvent.ITEM_EVENT)
                .data(mapper.writeValueAsString(messageOutput))
                .build();

            messageRepository.sendMessage(MARKET_QUEUE, mapper.writeValueAsString(commonMessageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private CreateItemOutput mountOutput(Item item) {
        return CreateItemOutput.toOutput(item);
    }
}
