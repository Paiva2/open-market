package org.com.openmarket.items.infra.persistence.repository.message;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.interfaces.repository.UserDataMessageRepository;
import org.com.openmarket.items.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.items.application.config.constants.QueueConstants.UserItem.USER_DATA_ITEM_QUEUE;

@Component
@AllArgsConstructor
public class UserDataMessageRepositoryImpl implements UserDataMessageRepository {
    private final UserDataMessageRepositoryOrm repository;

    public List<UserDataMessageEntity> getMessagesNotRead() {
        return repository.findAllNotReadByQueueNameOrderByCreatedAtDesc(USER_DATA_ITEM_QUEUE);
    }

    public List<UserDataMessageEntity> saveAll(List<UserDataMessageEntity> messageEntities) {
        return repository.saveAll(messageEntities);
    }
}
