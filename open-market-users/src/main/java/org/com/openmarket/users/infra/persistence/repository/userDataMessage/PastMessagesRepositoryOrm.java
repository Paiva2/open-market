package org.com.openmarket.users.infra.persistence.repository.userDataMessage;

import org.com.openmarket.users.infra.persistence.entity.PastMessagesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PastMessagesRepositoryOrm extends MongoRepository<PastMessagesEntity, String> {
}
