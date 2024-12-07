package org.com.openmarket.items.core.domain.usecase.item.listItems;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemRepository;
import org.com.openmarket.items.core.domain.usecase.item.listItems.dto.ListItemsInput;
import org.com.openmarket.items.core.domain.usecase.item.listItems.dto.ListItemsOutput;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListItemsUsecase {
    private final ItemRepository itemRepository;

    public ListItemsOutput execute(ListItemsInput input) {
        if (input.getPage() < 1) {
            input.setPage(1);
        }

        if (input.getSize() < 5) {
            input.setSize(5);
        } else if (input.getSize() > 50) {
            input.setSize(50);
        }

        Page<Item> items = findItems(input);

        return mountOutput(items, input);
    }

    private Page<Item> findItems(ListItemsInput input) {
        return itemRepository.findAllItems(input.getPage(), input.getSize(), input.getName(), input.getCategory(), input.getActive(), input.getUnique(), input.getMaxPrice(), input.getMinPrice(), input.getDirection());
    }

    private ListItemsOutput mountOutput(Page<Item> items, ListItemsInput input) {
        return ListItemsOutput.toOutput(items, input);
    }
}
