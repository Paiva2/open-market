package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.view.WalletInformationsView;

import java.util.Optional;
import java.util.UUID;

public interface WalletInformationsViewRepository {
    Optional<WalletInformationsView> findByWalletId(UUID walletId);
}
