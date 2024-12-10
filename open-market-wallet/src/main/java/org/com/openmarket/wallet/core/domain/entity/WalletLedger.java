package org.com.openmarket.wallet.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.wallet.core.domain.enums.EnumTransactionType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WalletLedger {
    private UUID id;
    private EnumTransactionType transactionType;
    private BigDecimal value;
    private Date createdAt;
    private Date updatedAt;

    private Wallet wallet;
}
