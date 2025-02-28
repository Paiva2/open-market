package org.com.openmarket.market.domain.core.usecase.userItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.AttributeItem;
import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.interfaces.AttributeItemRepository;
import org.com.openmarket.market.domain.interfaces.ItemRepository;
import org.com.openmarket.market.domain.interfaces.UserItemRepository;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateUserItemUsecase {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final AttributeItemRepository attributeItemRepository;
    private final UserItemRepository userItemRepository;

    public void execute(CreateUserItemInput input) {
        User user = findUser(input.getExternalUserId());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Item item = findItem(input.getExternalItemId());

        AttributeItem attributeItem = fillAttributeItem(input);
        attributeItem = persistAttributeItem(attributeItem);

        UserItem userItem = fillUserItem(input, user, item, attributeItem);
        persistUserItem(userItem);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(String externalItemId) {
        return itemRepository.findByExternalId(externalItemId).orElseThrow(ItemNotFoundException::new);
    }

    private AttributeItem fillAttributeItem(CreateUserItemInput input) {
        return AttributeItem.builder()
            .attributes(input.getAttribute().getAttributes())
            .externalId(input.getAttribute().getExternalAttributeId())
            .build();
    }

    private AttributeItem persistAttributeItem(AttributeItem attributeItem) {
        return attributeItemRepository.persist(attributeItem);
    }

    private UserItem fillUserItem(CreateUserItemInput input, User user, Item item, AttributeItem attributeItem) {
        return UserItem.builder()
            .id(UserItem.KeyId.builder()
                .userId(user.getId())
                .itemId(item.getId())
                .attributeId(attributeItem.getId())
                .build()
            ).user(user)
            .item(item)
            .attribute(attributeItem)
            .quantity(input.getQuantity())
            .build();
    }

    private void persistUserItem(UserItem userItem) {
        userItemRepository.persist(userItem);
    }
}
