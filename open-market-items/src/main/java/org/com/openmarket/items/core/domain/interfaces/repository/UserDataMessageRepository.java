package org.com.openmarket.items.core.domain.interfaces.repository;

import org.com.openmarket.items.infra.persistence.entity.UserDataMessageEntity;

import java.util.List;

public interface UserDataMessageRepository {
    List<UserDataMessageEntity> getMessagesNotRead();

    List<UserDataMessageEntity> saveAll(List<UserDataMessageEntity> messageEntities);
}
