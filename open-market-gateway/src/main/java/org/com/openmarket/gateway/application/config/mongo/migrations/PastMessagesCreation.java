package org.com.openmarket.gateway.application.config.mongo.migrations;


import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import org.springframework.data.mongodb.core.MongoTemplate;

@ChangeUnit(id = "1-create-past-messages-collection", order = "1", author = "paiva")
public class PastMessagesCreation {
    @Execution
    public void init(MongoTemplate mongoTemplate) {
        mongoTemplate.createCollection("past-messages");
    }
    
    @RollbackExecution
    public void rollback(MongoTemplate mongoTemplate) {
        mongoTemplate.dropCollection("past-messages");
    }
}
