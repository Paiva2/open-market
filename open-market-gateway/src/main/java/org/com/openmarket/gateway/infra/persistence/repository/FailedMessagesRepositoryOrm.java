package org.com.openmarket.gateway.infra.persistence.repository;

import org.com.openmarket.gateway.infra.persistence.entity.FailedMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FailedMessagesRepositoryOrm extends MongoRepository<FailedMessageEntity, String> {
}
