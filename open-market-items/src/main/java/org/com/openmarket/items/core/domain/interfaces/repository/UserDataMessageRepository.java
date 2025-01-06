package org.com.openmarket.items.core.domain.interfaces.repository;

import org.com.openmarket.items.infra.persistence.entity.PastMessagesEntity;

import java.util.List;

public interface UserDataMessageRepository {
    List<PastMessagesEntity> getMessagesNotRead();

    List<PastMessagesEntity> saveAll(List<PastMessagesEntity> messageEntities);
}
