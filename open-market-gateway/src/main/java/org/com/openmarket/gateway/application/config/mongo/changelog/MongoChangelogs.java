package org.com.openmarket.gateway.application.config.mongo.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog
public class MongoChangelogs {
    @ChangeSet(order = "001", id = "1-create-past-messages-collection", author = "paiva")
    public void creatingInitialDb(MongoDatabase db) {
        db.createCollection("past-messages");
    }

    @ChangeSet(order = "002", id = "2-create-failed-messages-collection", author = "paiva")
    public void createFailedMessagesColl(MongoDatabase db) {
        db.createCollection("failed-messages");
    }
}
