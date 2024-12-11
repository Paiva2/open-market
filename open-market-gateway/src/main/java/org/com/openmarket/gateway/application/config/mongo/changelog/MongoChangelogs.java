package org.com.openmarket.gateway.application.config.mongo.changelog;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.client.MongoDatabase;

@ChangeLog
public class MongoChangelogs {
    @ChangeSet(order = "001", id = "1-create-messages-user-data-collection", author = "paiva")
    public void creatingInitialDb(MongoDatabase db){
         db.createCollection("messages-user-data");
    }
}
