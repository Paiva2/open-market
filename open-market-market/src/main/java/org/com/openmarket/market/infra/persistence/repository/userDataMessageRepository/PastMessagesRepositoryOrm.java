package org.com.openmarket.market.infra.persistence.repository.userDataMessageRepository;

import org.com.openmarket.market.infra.persistence.entity.PastMessagesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PastMessagesRepositoryOrm extends MongoRepository<PastMessagesEntity, String> {
    @Query("{'queueName': ?0, 'queuesAlreadyReceived': {'$nin': [?1]}}")
    List<PastMessagesEntity> findAllNotReadByQueueNameAndQueueNotReceivedOrderByCreatedAtDesc(String queueName, String queueNotReceived);
}