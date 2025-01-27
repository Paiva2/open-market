package org.com.openmarket.market.domain.core.usecase.item.updateItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.ItemCategory;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemNotActiveException;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemNotFoundException;
import org.com.openmarket.market.domain.core.usecase.item.updateItem.dto.UpdateItemInput;
import org.com.openmarket.market.domain.interfaces.CategoryRepository;
import org.com.openmarket.market.domain.interfaces.ItemCategoryRepository;
import org.com.openmarket.market.domain.interfaces.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UpdateItemUsecase {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    public void execute(UpdateItemInput input) {
        Item item = findItem(input.getId());

        if (!item.getActive()) {
            throw new ItemNotActiveException();
        }

        List<ItemCategory> itemCategories = findItemCategories(item);
        checkItemCategoryUpdate(input, itemCategories, item);

        updateItem(input, item);
        persistItem(item);
    }

    private Item findItem(String externalItemId) {
        return itemRepository.findByExternalId(externalItemId).orElseThrow(ItemNotFoundException::new);
    }

    private void updateItem(UpdateItemInput input, Item item) {
        item.setName(input.getName());
        item.setDescription(input.getDescription());
        item.setPhotoUrl(input.getPhotoUrl());
        item.setUnique(input.getUnique());
        item.setBaseSellingPrice(input.getBaseSellingPrice());
        item.setActive(input.getActive());
    }

    private void persistItem(Item item) {
        itemRepository.save(item);
    }

    private List<ItemCategory> findItemCategories(Item item) {
        return itemCategoryRepository.findAllByItem(item.getId());
    }

    private void checkItemCategoryUpdate(UpdateItemInput input, List<ItemCategory> itemCategories, Item item) {
        List<String> currentCategoryExternalIds = itemCategories.stream().map(ItemCategory::getCategory).map(Category::getExternalId).toList();
        List<String> updateCategoriesExternalIds = input.getCategoriesIds();

        if (updateCategoriesExternalIds.isEmpty()) {
            removeAllCurrentItemCategory(item);
            return;
        }

        boolean hasNewIds = updateCategoriesExternalIds.stream().anyMatch(updatedId -> !currentCategoryExternalIds.contains(updatedId));

        if (hasNewIds || currentCategoryExternalIds.size() != updateCategoriesExternalIds.size()) {
            removeAllCurrentItemCategory(item);
            updateItemCategories(item, updateCategoriesExternalIds);
        }
    }

    private void removeAllCurrentItemCategory(Item item) {
        itemCategoryRepository.removeAllByItem(item.getId());
    }

    private void updateItemCategories(Item item, List<String> updateCategoriesExternalIds) {
        List<ItemCategory> itemCategories = new ArrayList<>();
        List<Category> categories = categoryRepository.findCategoriesByExternalId(updateCategoriesExternalIds);

        for (Category category : categories) {
            ItemCategory itemCategory = ItemCategory.builder()
                .id(ItemCategory.KeyId.builder()
                    .itemId(item.getId())
                    .categoryId(category.getId())
                    .build()
                ).item(item)
                .category(category)
                .build();

            itemCategories.add(itemCategory);
        }

        if (!itemCategories.isEmpty()) {
            List<ItemCategory> itemCategoriesUpdated = itemCategoryRepository.saveAll(itemCategories);
            item.setItemCategories(itemCategoriesUpdated);
        }
    }
}
