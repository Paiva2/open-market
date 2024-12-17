package org.com.openmarket.wallet.infra.persistence.mapper;

import org.com.openmarket.wallet.core.domain.entity.WalletLedger;
import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.springframework.beans.BeanUtils;

public class WalletLedgerMapper {
    public static WalletLedger toDomain(WalletLedgerEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        WalletLedger ledger = new WalletLedger();
        copyProperties(persistenceEntity, ledger);

        return ledger;
    }

    public static WalletLedgerEntity toPersistence(WalletLedger domainEntity) {
        if (domainEntity == null) return null;

        WalletLedgerEntity walletLedgerEntity = new WalletLedgerEntity();
        copyProperties(domainEntity, walletLedgerEntity);

        if (domainEntity.getWallet() != null) {
            WalletEntity walletEntity = new WalletEntity();
            copyProperties(domainEntity.getWallet(), walletEntity);

            walletLedgerEntity.setWallet(walletEntity);
        }

        if (domainEntity.getTargetWallet() != null) {
            WalletEntity targetWalletEntity = new WalletEntity();
            copyProperties(domainEntity.getTargetWallet(), targetWalletEntity);

            walletLedgerEntity.setTargetWallet(targetWalletEntity);
        }

        return walletLedgerEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
