package org.com.openmarket.wallet.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    private UUID id;
    private Date createdAt;
    private Date updatedAt;

    private User user;
    private List<WalletLedger> ledgers;
}
