package org.com.openmarket.wallet.infra.persistence.repository.wallet;

import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepositoryOrm extends JpaRepository<WalletEntity, UUID> {
    Optional<WalletEntity> findByUserId(UUID userId);
}
