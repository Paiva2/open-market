package org.com.openmarket.wallet.application.gateway.controller.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletMessageDTO {
    private String externalUserId;
    private NewTransaction transaction;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewTransaction {
        private UUID targetWalletId;
        private String description;
        private BigDecimal value;
        private String type;
    }
}
