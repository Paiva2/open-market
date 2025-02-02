package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseLock {
    private String id;
    private String databaseName;
    private String externalUserId;

    public DatabaseLock(String databaseName, String externalUserId) {
        this.databaseName = databaseName;
        this.externalUserId = externalUserId;
    }
}
