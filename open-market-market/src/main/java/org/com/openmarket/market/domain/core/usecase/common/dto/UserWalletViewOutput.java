package org.com.openmarket.market.domain.core.usecase.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWalletViewOutput {
    private UUID id;
    private BigDecimal balance;
    private Date updatedAt;

}
