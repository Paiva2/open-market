package org.com.openmarket.wallet.core.domain.entity.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletInformationsView {
    private UUID id;
    private Date lastUpdate;
    private Integer currentBalance;
}
