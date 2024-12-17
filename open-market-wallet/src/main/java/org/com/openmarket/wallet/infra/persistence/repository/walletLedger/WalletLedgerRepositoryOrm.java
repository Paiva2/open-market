package org.com.openmarket.wallet.infra.persistence.repository.walletLedger;

import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletLedgerRepositoryOrm extends JpaRepository<WalletLedgerEntity, UUID> {
}
