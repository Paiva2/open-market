package org.com.openmarket.users.infra.persistence.repository.userDataMessage;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.core.domain.repository.PastMessagesRepository;
import org.com.openmarket.users.infra.persistence.entity.PastMessagesEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PastMessagesRepositoryImpl implements PastMessagesRepository {
    private final PastMessagesRepositoryOrm repository;

    public void insertMessageRegistry(PastMessagesEntity entity) {
        repository.save(entity);
    }
}
