package org.com.openmarket.wallet.infra.persistence.repository.message;

import org.com.openmarket.wallet.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserDataMessageRepositoryOrm extends MongoRepository<UserDataMessageEntity, String> {
    @Query("{'queuesAlreadyReceived': {'$nin': [?0]}}")
    List<UserDataMessageEntity> findAllNotReadByQueueNameOrderByCreatedAtDesc(String queueName);

}