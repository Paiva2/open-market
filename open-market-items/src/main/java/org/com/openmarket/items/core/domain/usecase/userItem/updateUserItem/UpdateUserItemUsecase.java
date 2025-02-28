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
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotActiveException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.userItem.updateUserItem.dto.UpdateUserItemInput;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UpdateUserItemUsecase {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;

    @Transactional
    public void execute(UpdateUserItemInput input) {
        User user = findUser(input.getExternalUserId());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Item item = findItem(input.getExternalItemId());

        if (!item.getActive()) {
            throw new ItemNotActiveException();
        }

        UserItem userItem = findUserItem(user.getId(), item.getId(), input.getExternalAttributeId());

        if (input.getUserItemInput().getUserId() != null) {
            User newUser = findUser(input.getUserItemInput().getUserId());

            deleteCurrentUserItem(userItem);
            UserItem newUserItem = fillUserItem(userItem, input, newUser);
            persistUserItem(newUserItem);
        } else {
            updateUserItem(userItem, input);
            persistUserItem(userItem);
        }
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(Long.valueOf(externalUserId)).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(String externalItemId) {
        return itemRepository.findById(UUID.fromString(externalItemId)).orElseThrow(ItemNotFoundException::new);
    }

    private UserItem findUserItem(UUID userId, UUID itemId, String externalAttributeId) {
        return userItemRepository.findUserItem(userId, itemId, UUID.fromString(externalAttributeId)).orElseThrow(UserNotFoundException::new);
    }

    private void deleteCurrentUserItem(UserItem userItem) {
        userItemRepository.remove(userItem);
    }

    private UserItem fillUserItem(UserItem userItem, UpdateUserItemInput input, User user) {
        return UserItem.builder()
            .id(UserItem.KeyId.builder()
                .userId(user.getId())
                .itemId(userItem.getItem().getId())
                .attributeId(userItem.getAttribute().getId())
                .build()
            ).user(user)
            .attribute(userItem.getAttribute())
            .item(userItem.getItem())
            .quantity(input.getUserItemInput().getQuantity())
            .build();
    }

    private void updateUserItem(UserItem userItem, UpdateUserItemInput input) {
        userItem.setQuantity(input.getUserItemInput().getQuantity());
    }

    private UserItem persistUserItem(UserItem userItem) {
        return userItemRepository.save(userItem);
    }
}
