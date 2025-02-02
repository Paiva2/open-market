package org.com.openmarket.market.infra.persistence.repository.cache;

import org.com.openmarket.market.infra.persistence.entity.DatabaseLockEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface DatabaseLockRepositoryOrm extends CrudRepository<DatabaseLockEntity, String> {
    Optional<DatabaseLockEntity> findByDatabaseNameAndExternalUserId(String databaseName, String externalUserId);
}
