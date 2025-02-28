package org.com.openmarket.items.core.domain.usecase.userItem.createUserItem;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.AttributeItem;
import org.com.openmarket.items.core.domain.entity.Item;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.core.domain.interfaces.repository.*;
import org.com.openmarket.items.core.domain.usecase.common.dto.CreateUserItemMessageInput;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserDisabledException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.item.common.exception.ItemNotFoundException;
import org.com.openmarket.items.core.domain.usecase.userItem.createUserItem.dto.CreateUserItemInput;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static org.com.openmarket.items.application.config.constants.QueueConstants.Market.MARKET_QUEUE;

@Service
@AllArgsConstructor
public class CreateUserItemUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final AttributeItemRepository attributeItemRepository;
    private final MessageRepository messageRepository;

    public void execute(CreateUserItemInput input) {
        User user = findUser(input.getExternalUserId());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Item item = findItem(input.getExternalItemId());

        AttributeItem attributeItem = fillAttributeIem(input);
        attributeItem = persistAttributeItem(attributeItem);

        UserItem userItem = fillUserItem(user, item, attributeItem);
        userItem = persistUserItem(userItem);

        userItem.setAttribute(attributeItem);

        sendMessage(userItem);
    }

    private User findUser(String userId) {
        Optional<User> user = userRepository.findByExternalId(Long.valueOf(userId));

        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        return user.get();
    }

    private Item findItem(String itemId) {
        Optional<Item> item = itemRepository.findById(UUID.fromString(itemId));

        if (item.isEmpty()) {
            throw new ItemNotFoundException();
        }

        return item.get();
    }

    private AttributeItem persistAttributeItem(AttributeItem attributeItem) {
        return attributeItemRepository.save(attributeItem);
    }

    private AttributeItem fillAttributeIem(CreateUserItemInput input) {
        return AttributeItem.builder()
            .attributes(input.getItemAttribute().getAttribute())
            .build();
    }

    private UserItem fillUserItem(User user, Item item, AttributeItem attributeItem) {
        return UserItem.builder()
            .id(UserItem.KeyId.builder()
                .userId(user.getId())
                .itemId(item.getId())
                .attributeId(attributeItem.getId())
                .build()
            ).attribute(attributeItem)
            .user(user)
            .item(item)
            .build();
    }

    private UserItem persistUserItem(UserItem userItem) {
        return userItemRepository.save(userItem);
    }

    private void sendMessage(UserItem userItem) {
        try {
            CreateUserItemMessageInput input = CreateUserItemMessageInput.builder()
                .externalUserId(userItem.getUser().getExternalId())
                .externalItemId(userItem.getItem().getId().toString())
                .quantity(userItem.getQuantity())
                .attribute(CreateUserItemMessageInput.AttributeItemOutput.builder()
                    .externalAttributeId(userItem.getAttribute().getId().toString())
                    .attributes(userItem.getAttribute().getAttributes())
                    .build()
                ).build();

            messageRepository.sendMessage(MARKET_QUEUE, mapper.writeValueAsString(input));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
