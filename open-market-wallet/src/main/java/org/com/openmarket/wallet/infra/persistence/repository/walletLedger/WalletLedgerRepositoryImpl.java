package org.com.openmarket.wallet.infra.persistence.repository.walletLedger;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.WalletLedger;
import org.com.openmarket.wallet.core.interfaces.WalletLedgerRepository;
import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.WalletLedgerMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WalletLedgerRepositoryImpl implements WalletLedgerRepository {
    private final WalletLedgerRepositoryOrm repository;

    @Override
    public WalletLedger save(WalletLedger walletLedger) {
        WalletLedgerEntity walletLedgerEntity = repository.save(WalletLedgerMapper.toPersistence(walletLedger));
        return WalletLedgerMapper.toDomain(walletLedgerEntity);
    }
}
