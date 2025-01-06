package org.com.openmarket.wallet.infra.persistence.repository.message;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.interfaces.UserDataMessageRepository;
import org.com.openmarket.wallet.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.com.openmarket.wallet.application.config.constants.QueueConstants.WALLET_QUEUE;

@Component
@AllArgsConstructor
public class UserDataMessageRepositoryImpl implements UserDataMessageRepository {
    private final UserDataMessageRepositoryOrm repository;

    @Override
    public List<UserDataMessageEntity> getMessagesNotRead() {
        return repository.findAllNotReadByQueueNameOrderByCreatedAtDesc(WALLET_QUEUE);
    }

    @Override
    public List<UserDataMessageEntity> saveAll(List<UserDataMessageEntity> userDataMessageEntities) {
        return repository.saveAll(userDataMessageEntities);
    }
}
