package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterNewTransactionInput {
    private String externalUserId;
    private UUID targetWalletId;
    private String description;
    private BigDecimal value;
    private String type;
}
