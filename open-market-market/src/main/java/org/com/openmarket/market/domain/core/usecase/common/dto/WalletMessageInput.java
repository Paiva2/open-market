package org.com.openmarket.market.domain.core.usecase.common.dto;

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
public class WalletMessageInput {
    private String externalUserId;
    private NewTransaction transaction;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewTransaction {
        private UUID targetWalletId;
        private String description;
        private BigDecimal value;
        private String type;
    }
}
