package org.com.openmarket.market.infra.persistence.repository.userDataMessageRepository;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.interfaces.PastMessagesRepository;
import org.com.openmarket.market.infra.persistence.entity.PastMessagesEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.market.application.config.constants.QueueConstants.MARKET_QUEUE;

@Component
@AllArgsConstructor
public class PastMessagesRepositoryImpl implements PastMessagesRepository {
    private final PastMessagesRepositoryOrm repository;

    @Override
    public List<PastMessagesEntity> getNonReadMessages() {
        return repository.findAllNotReadByQueueNameAndQueueNotReceivedOrderByCreatedAtDesc("user-queue", MARKET_QUEUE);
    }

    @Override
    public List<PastMessagesEntity> saveAll(List<PastMessagesEntity> messages) {
        return repository.saveAll(messages);
    }
}
