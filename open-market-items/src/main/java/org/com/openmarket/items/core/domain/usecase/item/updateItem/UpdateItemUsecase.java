package org.com.openmarket.items.core.domain.usecase.item.updateItem;

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
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.createItem.exception.ItemAlreadyExistsException;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.dto.UpdateItemInput;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.dto.UpdateItemMessageOutput;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.dto.UpdateItemOutput;
import org.com.openmarket.items.core.domain.usecase.itemAlteration.registerItemAlteration.RegisterItemAlterationUsecase;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static org.com.openmarket.items.application.config.constants.QueueConstants.Market.MARKET_QUEUE;

@Service
@AllArgsConstructor
public class UpdateItemUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;
    private final RegisterItemAlterationUsecase registerItemAlterationUsecase;
    private final MessageRepository messageRepository;

    public UpdateItemOutput execute(Long userId, UpdateItemInput input) {
        User user = findUser(userId);
        Item item = findItem(input.getId());

        checkNameAlreadyUsed(item, input.getName());

        if (input.getBaseSellingPrice().compareTo(new BigDecimal("0")) < 0) {
            throw new InvalidFieldException("baseSellingPrice can't be less than 0");
        }

        List<ItemCategory> itemCategories = findItemCategories(item.getId());
        List<ItemCategory> itemCategoriesUpdated = checkUpdatedCategories(item, itemCategories, input.getCategoriesIds());

        fillItemUpdated(input, item);
        item = persistItemUpdated(item);
        item.setItemCategories(itemCategoriesUpdated);

        registerItemAlteration(user, item);
        sendAlterationMessage(item);

        return mountOutput(item);
    }

    private User findUser(Long userId) {
        return userRepository.findByExternalId(userId).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(UUID itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    private void checkNameAlreadyUsed(Item currentItem, String newName) {
        if (currentItem.getName().equals(newName)) return;

        Optional<Item> itemWithName = itemRepository.findByName(newName);

        if (itemWithName.isPresent()) {
            throw new ItemAlreadyExistsException(newName);
        }
    }

    private List<ItemCategory> findItemCategories(UUID itemId) {
        return itemCategoryRepository.findAllByItem(itemId);
    }

    private List<ItemCategory> checkUpdatedCategories(Item item, List<ItemCategory> itemCategories, List<Long> newCategoriesIds) {
        newCategoriesIds = removeDuplicatedIds(newCategoriesIds);
        List<Long> currentCategoriesIds = itemCategories.stream().map(ItemCategory::getCategory).map(Category::getId).toList();
        boolean hasNotChangedCategories = currentCategoriesIds.size() == newCategoriesIds.size() && currentCategoriesIds.containsAll(newCategoriesIds);

        if (hasNotChangedCategories) return itemCategories;

        removeAllCurrentItemCategories(item.getId());
        List<Category> getCategories = findCategories(newCategoriesIds);
        List<ItemCategory> itemCategoriesUpdated = fillNewItemCategories(item, getCategories);
        itemCategoriesUpdated = persistNewItemCategories(itemCategoriesUpdated);

        return itemCategoriesUpdated;
    }

    private List<Long> removeDuplicatedIds(List<Long> ids) {
        return new ArrayList<>(new HashSet<>(ids));
    }

    private void removeAllCurrentItemCategories(UUID itemId) {
        itemCategoryRepository.removeAllByItem(itemId);
    }

    private List<Category> findCategories(List<Long> categoriesIds) {
        List<Category> categoriesFound = categoryRepository.findCategoriesById(categoriesIds);

        if (categoriesFound.isEmpty()) {
            throw new CategoryNotFoundException(MessageFormat.format("Categories with id {0} not found!", categoriesIds));
        }

        List<Long> categoriesFoundIds = categoriesFound.stream().map(Category::getId).toList();
        List<Long> categoriesNotFound = categoriesIds.stream().filter(id -> !categoriesFoundIds.contains(id)).toList();

        if (!categoriesNotFound.isEmpty()) {
            throw new CategoryNotFoundException(MessageFormat.format("Categories with id {0} not found!", categoriesNotFound));
        }

        return categoriesFound;
    }

    private List<ItemCategory> fillNewItemCategories(Item item, List<Category> categories) {
        List<ItemCategory> itemCategories = new ArrayList<>();

        for (Category category : categories) {
            ItemCategory.KeyId keyId = new ItemCategory.KeyId(item.getId(), category.getId());
            ItemCategory itemCategory = new ItemCategory(keyId, item, category);
            itemCategories.add(itemCategory);
        }

        return itemCategories;
    }

    private List<ItemCategory> persistNewItemCategories(List<ItemCategory> itemCategories) {
        return itemCategoryRepository.saveAll(itemCategories);
    }

    private void fillItemUpdated(UpdateItemInput input, Item item) {
        item.setDescription(input.getDescription() != null ? input.getDescription() : item.getDescription());
        item.setName(input.getName() != null ? input.getName() : item.getName());
        item.setPhotoUrl(input.getPhotoUrl() != null ? input.getPhotoUrl() : item.getPhotoUrl());
        item.setUnique(input.getUnique() != null ? input.getUnique() : item.getUnique());
        item.setBaseSellingPrice(input.getBaseSellingPrice() != null ? input.getBaseSellingPrice() : item.getBaseSellingPrice());
        item.setActive(input.getActive() != null ? input.getActive() : item.getActive());
    }

    private Item persistItemUpdated(Item item) {
        return itemRepository.save(item);
    }

    private void registerItemAlteration(User user, Item item) {
        registerItemAlterationUsecase.execute(user, item, EnumItemAlteration.UPDATE);
    }

    private void sendAlterationMessage(Item item) {
        try {
            UpdateItemMessageOutput messageOutput = UpdateItemMessageOutput.builder()
                .id(item.getId().toString())
                .name(item.getName())
                .description(item.getDescription())
                .photoUrl(item.getPhotoUrl())
                .unique(item.getUnique())
                .baseSellingPrice(item.getBaseSellingPrice())
                .active(item.getActive())
                .categoriesIds(item.getItemCategories().stream().map(ItemCategory::getCategory).map(category -> category.getId().toString()).toList())
                .build();

            CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.UPDATED)
                .event(EnumMessageEvent.ITEM_EVENT)
                .data(mapper.writeValueAsString(messageOutput))
                .build();

            messageRepository.sendMessage(MARKET_QUEUE, mapper.writeValueAsString(commonMessageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UpdateItemOutput mountOutput(Item item) {
        return UpdateItemOutput.toOutput(item);
    }
}
