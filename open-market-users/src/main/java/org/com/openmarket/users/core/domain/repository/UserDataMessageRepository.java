package org.com.openmarket.users.core.domain.repository;

import org.com.openmarket.users.infra.persistence.entity.UserDataMessageEntity;

public interface UserDataMessageRepository {
    void insertMessageRegistry(UserDataMessageEntity entity);
}
