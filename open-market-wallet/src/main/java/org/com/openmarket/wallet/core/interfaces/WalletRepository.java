package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.Wallet;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {
    Wallet save(Wallet wallet);

    Optional<Wallet> findByUserId(UUID userId);

    Optional<Wallet> findById(UUID walletId);

    Optional<Wallet> findByExternalUserId(String externalUserId);
}
