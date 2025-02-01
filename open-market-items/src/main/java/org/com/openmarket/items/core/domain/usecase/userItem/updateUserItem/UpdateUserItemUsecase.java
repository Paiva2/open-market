package org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserItemRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserDisabledException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotActiveException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto.UpdateUserItemInput;
import org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto.UpdateUserItemOutput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UpdateUserItemUsecase {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Transactional
    public UpdateUserItemOutput execute(UpdateUserItemInput input) {
        User user = findUser(input.getExternalUserId());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Item item = findItem(input.getExternalItemId());

        if (!item.getActive()) {
            throw new ItemNotActiveException();
        }

        UserItem userItem = findUserItem(user.getId(), item.getId(), input.getExternalAttributeId());
        updateUserItem(userItem, input);
        userItem = persistUserItem(userItem);

        return mountOutput(userItem);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(Long.valueOf(externalUserId)).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(String externalItemId) {
        return itemRepository.findById(UUID.fromString(externalItemId)).orElseThrow(ItemNotFoundException::new);
    }

    private UserItem findUserItem(UUID userId, UUID itemId, String externalAttributeId) {
        return userItemRepository.findUserItem(userId, itemId, UUID.fromString(externalAttributeId)).orElseThrow(UserItemNotFoundException::new);
    }

    private void updateUserItem(UserItem userItem, UpdateUserItemInput input) {
        userItem.setQuantity(input.getUserItemInput().getQuantity());
    }

    private UserItem persistUserItem(UserItem userItem) {
        return userItemRepository.save(userItem);
    }

    private UpdateUserItemOutput mountOutput(UserItem userItem) {
        return UpdateUserItemOutput.toOutput(userItem);
    }
}
