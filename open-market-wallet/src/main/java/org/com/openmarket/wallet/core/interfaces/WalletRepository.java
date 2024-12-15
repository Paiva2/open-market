package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.Wallet;

public interface WalletRepository {
    Wallet save(Wallet wallet);
}
