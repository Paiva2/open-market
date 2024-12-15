package org.com.openmarket.wallet.infra.persistence.repository.wallet;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.WalletMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    private final WalletRepositoryOrm repository;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity walletEntity = repository.save(WalletMapper.toPersistence(wallet));
        return WalletMapper.toDomain(walletEntity);
    }
}
