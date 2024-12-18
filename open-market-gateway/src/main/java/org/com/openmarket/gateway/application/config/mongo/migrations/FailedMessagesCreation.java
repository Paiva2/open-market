package org.com.openmarket.gateway.application.config.mongo.migrations;

import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "2-create-failed-messages-collection", order = "2", author = "paiva")
public class FailedMessagesCreation {
    @Execution
    public void init(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("failed-messages");
    }
    
    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("failed-messages");
    }
}
