package org.com.openmarket.market.domain.core.usecase.item.crateItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Category;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.ItemCategory;
import org.com.openmarket.market.domain.core.usecase.common.exception.CategoryNotFoundException;
import org.com.openmarket.market.domain.core.usecase.item.crateItem.dto.CreateItemInput;
import org.com.openmarket.market.domain.core.usecase.item.crateItem.exception.ItemAlreadyExistsException;
import org.com.openmarket.market.domain.interfaces.CategoryRepository;
import org.com.openmarket.market.domain.interfaces.ItemCategoryRepository;
import org.com.openmarket.market.domain.interfaces.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CreateItemUsecase {
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    public void execute(CreateItemInput input) {
        checkItemAlreadyExists(input.getName());
        List<Category> categories = findCategories(input.getCategoriesIds().stream().map(Object::toString).toList());

        Item item = fillItem(input);
        item = persistItem(item);

        List<ItemCategory> itemCategories = fillItemCategories(item, categories);
        persistItemCategories(itemCategories);
    }

    private void checkItemAlreadyExists(String itemName) {
        Optional<Item> item = itemRepository.findByName(itemName);

        if (item.isPresent()) {
            throw new ItemAlreadyExistsException(itemName);
        }
    }

    private List<Category> findCategories(List<String> categoriesIds) {
        List<Category> categories = categoryRepository.findCategoriesByExternalId(categoriesIds);

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Categories not found: " + categoriesIds);
        }

        if (categories.size() != categoriesIds.size()) {
            List<String> categoriesFoundExtIds = categories.stream().map(Category::getExternalId).toList();
            List<String> categoriesNotFoundExtIds = categoriesIds.stream().filter(catId -> !categoriesFoundExtIds.contains(catId)).toList();

            if (!categoriesNotFoundExtIds.isEmpty()) {
                throw new CategoryNotFoundException("Categories not found: " + categoriesNotFoundExtIds);
            }
        }

        return categories;
    }

    private Item fillItem(CreateItemInput input) {
        return Item.builder()
            .externalId(input.getId().toString())
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

    private void persistItemCategories(List<ItemCategory> itemCategories) {
        itemCategoryRepository.saveAll(itemCategories);
    }
}
