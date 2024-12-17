package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.WalletLedger;

public interface WalletLedgerRepository {
    WalletLedger save(WalletLedger walletLedger);
}
