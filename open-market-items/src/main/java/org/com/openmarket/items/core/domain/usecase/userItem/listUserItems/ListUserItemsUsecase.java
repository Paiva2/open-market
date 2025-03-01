package org.com.openmarket.items.core.domain.usecase.userItem.listUserItems;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.entity.UserItem;
import org.com.openmarket.items.core.domain.interfaces.repository.UserItemRepository;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.dto.PageableList;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserDisabledException;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.items.core.domain.usecase.userItem.listUserItems.dto.UserItemOutput;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ListUserItemsUsecase {
    private final UserRepository userRepository;
    private final UserItemRepository userItemRepository;

    public PageableList<UserItemOutput> execute(Long externalUserId, int page, int size) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (page < 1) {
            page = 1;
        }

        if (size < 5) {
            size = 5;
        }

        PageableList<UserItem> userItems = findUserItems(user, page, size);

        return mountOutput(userItems);
    }

    private User findUser(Long externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private PageableList<UserItem> findUserItems(User user, int page, int size) {
        return userItemRepository.findAllByUser(user.getId(), page, size);
    }

    private PageableList<UserItemOutput> mountOutput(PageableList<UserItem> userItems) {
        return new PageableList<>(
            userItems.getPage(),
            userItems.getSize(),
            userItems.getTotalItems(),
            userItems.getTotalPages(),
            userItems.isLast(),
            userItems.getData().stream().map(UserItemOutput::new).toList()
        );
    }
}
