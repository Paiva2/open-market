package org.com.openmarket.wallet.infra.persistence.repository.wallet;

import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryOrm extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(UUID userId);

    @Query("select wal from WalletEntity wal where wal.user.externalId = :externalUserId")
    Optional<WalletEntity> findByExternalUserId(@Param("externalUserId") String externalUserId);
}
