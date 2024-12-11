package org.com.openmarket.users.infra.persistence.repository.userDataMessage;

import org.com.openmarket.users.infra.persistence.entity.UserDataMessageEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDataMessageOrm extends MongoRepository<UserDataMessageEntity, String> {
}
