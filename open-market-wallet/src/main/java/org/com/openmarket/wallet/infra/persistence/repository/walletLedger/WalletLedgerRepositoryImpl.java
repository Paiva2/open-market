package org.com.openmarket.wallet.infra.persistence.repository.walletLedger;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.WalletLedger;
import org.com.openmarket.wallet.core.domain.usecase.common.dto.WalletLedgerListPagedOutput;
import org.com.openmarket.wallet.core.interfaces.WalletLedgerRepository;
import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.WalletLedgerMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Component
@AllArgsConstructor
public class WalletLedgerRepositoryImpl implements WalletLedgerRepository {
    private final WalletLedgerRepositoryOrm repository;

    @Override
    public WalletLedger save(WalletLedger walletLedger) {
        WalletLedgerEntity walletLedgerEntity = repository.save(WalletLedgerMapper.toPersistence(walletLedger));
        return WalletLedgerMapper.toDomain(walletLedgerEntity);
    }

    @Override
    public WalletLedgerListPagedOutput listPaged(UUID walletId, int page, int size, Date from, Date to, BigDecimal max, BigDecimal min, String direction) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.Direction.valueOf(direction), "wlg_created_at");
        Page<WalletLedgerEntity> entitiesPage = repository.findAllFilteringByWalletId(walletId, from, to, min, max, pageRequest);
        return WalletLedgerListPagedOutput.toDomain(entitiesPage);
    }
}
