package org.com.openmarket.items.infra.persistence.repository.mongo.message;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.interfaces.repository.UserDataMessageRepository;
import org.com.openmarket.items.infra.persistence.entity.PastMessagesEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.items.application.config.constants.QueueConstants.ITEM_QUEUE;


@Component
@AllArgsConstructor
public class PastMessagesRepositoryImpl implements UserDataMessageRepository {
    private final PastMessagesRepositoryOrm repository;

    public List<PastMessagesEntity> getMessagesNotRead() {
        return repository.findAllNotReadByQueueNameAndQueueNotReceivedOrderByCreatedAtDesc("user-queue", ITEM_QUEUE);
    }

    public List<PastMessagesEntity> saveAll(List<PastMessagesEntity> messageEntities) {
        return repository.saveAll(messageEntities);
    }
}
