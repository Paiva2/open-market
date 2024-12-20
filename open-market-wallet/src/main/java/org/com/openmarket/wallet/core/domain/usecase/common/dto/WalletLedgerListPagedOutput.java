package org.com.openmarket.wallet.core.domain.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.wallet.core.enums.EnumTransactionType;
import org.com.openmarket.wallet.infra.persistence.entity.WalletLedgerEntity;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletLedgerListPagedOutput {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private boolean isLast;
    private List<Ledger> ledgers;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Ledger {
        private UUID id;
        private EnumTransactionType transactionType;
        private BigDecimal value;
        private UUID targetWalletId;
        private UUID sourceWalletId;
        private String description;
        private Date createdAt;
        private Date updatedAt;

        public Ledger(WalletLedgerEntity ledgerEntity) {
            this.id = ledgerEntity.getId();
            this.transactionType = ledgerEntity.getTransactionType();
            this.value = ledgerEntity.getValue().divide(BigDecimal.valueOf(100));
            this.description = ledgerEntity.getDescription();
            this.createdAt = ledgerEntity.getCreatedAt();
            this.updatedAt = ledgerEntity.getUpdatedAt();
            this.sourceWalletId = ledgerEntity.getWallet().getId();
            this.targetWalletId = ledgerEntity.getTargetWallet().getId();
        }
    }

    public static WalletLedgerListPagedOutput toDomain(Page<WalletLedgerEntity> pageList) {
        return WalletLedgerListPagedOutput.builder()
            .page(pageList.getNumber() + 1)
            .size(pageList.getSize())
            .totalItems(pageList.getTotalElements())
            .totalPages(pageList.getTotalPages())
            .isLast(pageList.isLast())
            .ledgers(pageList.get().map(Ledger::new).toList())
            .build();
    }
}
