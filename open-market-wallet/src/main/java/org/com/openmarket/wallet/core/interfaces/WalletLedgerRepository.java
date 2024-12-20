package org.com.openmarket.wallet.core.interfaces;

import org.com.openmarket.wallet.core.domain.entity.WalletLedger;
import org.com.openmarket.wallet.core.domain.usecase.common.dto.WalletLedgerListPagedOutput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface WalletLedgerRepository {
    WalletLedger save(WalletLedger walletLedger);

    WalletLedgerListPagedOutput listPaged(UUID walletId, int page, int size, Date from, Date to, BigDecimal max, BigDecimal min, String direction);
}
