package org.com.openmarket.wallet.infra.persistence.repository.wallet;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.WalletMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class WalletRepositoryImpl implements WalletRepository {
    private final WalletRepositoryOrm repository;

    @Override
    public Wallet save(Wallet wallet) {
        WalletEntity walletEntity = repository.save(WalletMapper.toPersistence(wallet));
        return WalletMapper.toDomain(walletEntity);
    }

    @Override
    public Optional<Wallet> findByUserId(UUID userId) {
        Optional<WalletEntity> walletEntity = repository.findByUserId(userId);
        if (walletEntity.isEmpty()) return Optional.empty();
        return Optional.of(WalletMapper.toDomain(walletEntity.get()));
    }

    @Override
    public Optional<Wallet> findById(UUID walletId) {
        Optional<WalletEntity> walletEntity = repository.findById(walletId);
        if (walletEntity.isEmpty()) return Optional.empty();
        return Optional.of(WalletMapper.toDomain(walletEntity.get()));
    }
}
