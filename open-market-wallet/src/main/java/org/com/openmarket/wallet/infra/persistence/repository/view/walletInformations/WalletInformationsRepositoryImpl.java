package org.com.openmarket.wallet.infra.persistence.repository.view.walletInformations;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.view.WalletInformationsView;
import org.com.openmarket.wallet.core.interfaces.WalletInformationsViewRepository;
import org.com.openmarket.wallet.infra.persistence.entity.view.WalletInformationsViewEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.WalletInformationsMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class WalletInformationsRepositoryImpl implements WalletInformationsViewRepository {
    private final WalletInformationsViewRepositoryOrm repository;

    @Override
    public Optional<WalletInformationsView> findByWalletId(UUID walletId) {
        Optional<WalletInformationsViewEntity> view = repository.findById(walletId);
        if (view.isEmpty()) return Optional.empty();
        return Optional.of(WalletInformationsMapper.toDomain(view.get()));
    }
}
