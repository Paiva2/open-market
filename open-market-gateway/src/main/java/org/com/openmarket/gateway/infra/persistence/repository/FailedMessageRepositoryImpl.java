package org.com.openmarket.gateway.infra.persistence.repository;

import lombok.AllArgsConstructor;
import org.com.openmarket.gateway.core.domain.entity.FailedMessage;
import org.com.openmarket.gateway.core.interfaces.FailedMessageRepository;
import org.com.openmarket.gateway.infra.persistence.entity.FailedMessageEntity;
import org.com.openmarket.gateway.infra.persistence.mapper.FailedMessageMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class FailedMessageRepositoryImpl implements FailedMessageRepository {
    private final FailedMessagesRepositoryOrm repository;

    @Override
    public FailedMessage save(FailedMessage failedMessage) {
        FailedMessageEntity entity = repository.save(FailedMessageMapper.toPersistence(failedMessage));
        return FailedMessageMapper.toDomain(entity);
    }
}
