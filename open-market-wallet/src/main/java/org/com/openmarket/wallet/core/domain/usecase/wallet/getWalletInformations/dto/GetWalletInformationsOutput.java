package org.com.openmarket.wallet.core.domain.usecase.wallet.getWalletInformations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.view.WalletInformationsView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetWalletInformationsOutput {
    private UUID id;
    private BigDecimal balance;
    private Date updatedAt;

    public static GetWalletInformationsOutput toOutput(WalletInformationsView view) {
        return GetWalletInformationsOutput.builder()
            .id(view.getId())
            .balance(BigDecimal.valueOf(view.getCurrentBalance()).divide(BigDecimal.valueOf(100)))
            .updatedAt(view.getLastUpdate())
            .build();
    }
}
