package org.com.openmarket.wallet.infra.persistence.repository.walletLedger;

import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public interface WalletLedgerRepositoryOrm extends JpaRepository<WalletLedgerEntity, UUID> {
    @Query(value = """
        select * from tb_wallets_ledgers wlg
        join tb_wallets wlt on wlt.wlt_id = wlg.wlg_wallet_id
        where (wlg.wlg_wallet_id = :walletId or wlg.wlg_target_wallet_id = :walletId)
        and ((:min is null and :max is null) or (:min is null or (wlg_value::numeric(12, 2) / 100 >= cast(:min as numeric(12, 2)))) and (:max is null or (wlg_value::numeric(12, 2) / 100) <= cast(:max as numeric(12, 2))))
        and ((cast(:from as date) is null and cast(:to as date) is null) or (cast(:from as date) is null or date_trunc('day', wlg.wlg_created_at) >= date_trunc('day', cast(:from as date))) and (cast(:to as date) is null or date_trunc('day', wlg.wlg_created_at) <= date_trunc('day', cast(:to as date))))
        """, nativeQuery = true)
    Page<WalletLedgerEntity> findAllFilteringByWalletId(@Param("walletId") UUID walletId, @Param("from") Date from, @Param("to") Date to, @Param("min") BigDecimal min, @Param("max") BigDecimal max, Pageable pageable);
}
