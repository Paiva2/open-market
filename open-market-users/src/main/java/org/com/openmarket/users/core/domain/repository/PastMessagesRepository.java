package org.com.openmarket.users.core.domain.repository;

import org.com.openmarket.users.infra.persistence.entity.PastMessagesEntity;

public interface PastMessagesRepository {
    void insertMessageRegistry(PastMessagesEntity entity);
}
