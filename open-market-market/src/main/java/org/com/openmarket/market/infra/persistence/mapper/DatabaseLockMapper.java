package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.DatabaseLock;
import org.com.openmarket.market.infra.persistence.entity.DatabaseLockEntity;
import org.springframework.beans.BeanUtils;

public class DatabaseLockMapper {
    public static DatabaseLock toDomain(DatabaseLockEntity entity) {
        if (entity == null) return null;

        DatabaseLock databaseLock = new DatabaseLock();
        copyProperties(entity, databaseLock);

        return databaseLock;
    }

    public static DatabaseLockEntity toPersistence(DatabaseLock entity) {
        if (entity == null) return null;

        DatabaseLockEntity databaseLock = new DatabaseLockEntity();
        copyProperties(entity, databaseLock);

        return databaseLock;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
