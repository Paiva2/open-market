package org.com.openmarket.wallet.infra.persistence.repository.view.walletInformations;

import org.com.openmarket.wallet.infra.persistence.entity.view.WalletInformationsViewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletInformationsViewRepositoryOrm extends JpaRepository<WalletInformationsViewEntity, UUID> {
    
}
