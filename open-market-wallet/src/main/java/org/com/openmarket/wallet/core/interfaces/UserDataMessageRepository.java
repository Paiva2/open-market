package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.infra.persistence.entity.UserDataMessageEntity;

import java.util.List;

public interface UserDataMessageRepository {
    List<UserDataMessageEntity> getMessagesNotRead();

    List<UserDataMessageEntity> saveAll(List<UserDataMessageEntity> userDataMessageEntities);
}
