package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "db_lock_entity", timeToLive = 60L) // TTL 5min
public class DatabaseLockEntity implements Serializable {
    @Id
    private String id;

    @Indexed
    private String databaseName;
}
