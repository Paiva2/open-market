package org.com.openmarket.wallet.core.domain.usecase.walletLedger.listWalletLedgers.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListWalletLedgersInput {
    private String externalId;
    private Integer page;
    private Integer size;
    private Date from;
    private Date to;
    private BigDecimal min;
    private BigDecimal max;
    private String direction;
}
