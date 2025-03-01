package org.com.openmarket.items.core.domain.usecase.item.findItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemRepository;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotActiveException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.findItem.dto.FindItemOutput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class FindItemUsecase {
    private final ItemRepository itemRepository;

    public FindItemOutput execute(UUID itemId) {
        Item item = findItem(itemId);

        if (!item.getActive()) {
            throw new ItemNotActiveException();
        }

        return mountOutput(item);
    }

    private Item findItem(UUID itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    private FindItemOutput mountOutput(Item item) {
        return FindItemOutput.builder()
            .id(item.getId())
            .name(item.getName())
            .description(item.getDescription())
            .photoUrl(item.getPhotoUrl())
            .unique(item.getUnique())
            .stackable(item.getStackable())
            .baseSellingPrice(item.getBaseSellingPrice())
            .active(item.getActive())
            .baseAttribute(FindItemOutput.BaseAttributeOutput.builder()
                .id(item.getBaseAttribute().getId())
                .attributes(item.getBaseAttribute().getAttributes())
                .build()
            ).build();
    }
}
