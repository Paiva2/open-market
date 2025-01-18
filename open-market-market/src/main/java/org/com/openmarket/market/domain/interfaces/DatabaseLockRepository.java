package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.entity.DatabaseLock;

import java.util.Optional;

public interface DatabaseLockRepository {
    DatabaseLock saveLock(DatabaseLock databaseLockEntity);

    Optional<DatabaseLock> getLockByDatabase(String databaseName);

    void removeLock(DatabaseLock databaseLockEntity);
}
