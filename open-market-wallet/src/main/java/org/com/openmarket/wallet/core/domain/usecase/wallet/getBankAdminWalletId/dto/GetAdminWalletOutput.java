package org.com.openmarket.wallet.core.domain.usecase.wallet.getBankAdminWalletId.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAdminWalletOutput {
    private String externalAdminId;
    private UUID walletId;
}
