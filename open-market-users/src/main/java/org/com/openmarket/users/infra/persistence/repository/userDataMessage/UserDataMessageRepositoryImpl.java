package org.com.openmarket.users.infra.persistence.repository.userDataMessage;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.core.domain.repository.UserDataMessageRepository;
import org.com.openmarket.users.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserDataMessageRepositoryImpl implements UserDataMessageRepository {
    private final UserDataMessageOrm repository;

    public void insertMessageRegistry(UserDataMessageEntity entity) {
        repository.save(entity);
    }
}
