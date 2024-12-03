package org.com.openmarket.items.core.domain.usecase.item.disableItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.enumeration.EnumItemAlteration;
import org.com.openmarket.items.core.domain.repository.ItemRepository;
import org.com.openmarket.items.core.domain.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotActiveException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.itemAlteration.registerItemAlteration.RegisterItemAlterationUsecase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DisableItemUsecase {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final RegisterItemAlterationUsecase registerItemAlterationUsecase;

    public void execute(Long extId, UUID itemId) {
        User user = findUser(extId);

        Item item = findItem(itemId);

        if (!item.getActive()) {
            throw new ItemNotActiveException();
        }

        disableItem(item);
        registerItemAlteration(user, item);
    }

    private User findUser(Long userId) {
        return userRepository.findByExternalId(userId).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(UUID itemId) {
        return itemRepository.findById(itemId).orElseThrow(ItemNotFoundException::new);
    }

    private void disableItem(Item item) {
        item.setActive(false);

        itemRepository.save(item);
    }

    private void registerItemAlteration(User user, Item item) {
        registerItemAlterationUsecase.execute(user, item, EnumItemAlteration.DELETE);
    }
}
