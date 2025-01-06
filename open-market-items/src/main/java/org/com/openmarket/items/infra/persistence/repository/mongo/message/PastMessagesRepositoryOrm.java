package org.com.openmarket.items.infra.persistence.repository.mongo.message;

import org.com.openmarket.items.infra.persistence.entity.PastMessagesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PastMessagesRepositoryOrm extends MongoRepository<PastMessagesEntity, String> {
    @Query("{'queueName': ?0, 'queuesAlreadyReceived': {'$nin':  [?1]}}")
    List<PastMessagesEntity> findAllNotReadByQueueNameAndQueueNotReceivedOrderByCreatedAtDesc(String queueName, String queueNotReceived);
}