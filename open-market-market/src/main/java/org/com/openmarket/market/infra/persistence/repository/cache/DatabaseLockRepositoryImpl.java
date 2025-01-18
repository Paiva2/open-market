package org.com.openmarket.market.infra.persistence.repository.cache;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.DatabaseLock;
import org.com.openmarket.market.domain.interfaces.DatabaseLockRepository;
import org.com.openmarket.market.infra.persistence.entity.DatabaseLockEntity;
import org.com.openmarket.market.infra.persistence.mapper.DatabaseLockMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class DatabaseLockRepositoryImpl implements DatabaseLockRepository {
    private final DatabaseLockRepositoryOrm repository;

    public DatabaseLock saveLock(DatabaseLock databaseLockEntity) {
        DatabaseLockEntity databaseLock = repository.save(DatabaseLockMapper.toPersistence(databaseLockEntity));
        return DatabaseLockMapper.toDomain(databaseLock);
    }

    public Optional<DatabaseLock> getLockByDatabase(String databaseName) {
        Optional<DatabaseLockEntity> databaseLock = repository.findByDatabaseName(databaseName);
        if (databaseLock.isEmpty()) return Optional.empty();
        return Optional.of(DatabaseLockMapper.toDomain(databaseLock.get()));
    }

    @Override
    public void removeLock(DatabaseLock databaseLockEntity) {
        repository.delete(DatabaseLockMapper.toPersistence(databaseLockEntity));
    }

}
