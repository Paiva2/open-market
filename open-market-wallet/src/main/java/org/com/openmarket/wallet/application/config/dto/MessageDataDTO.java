package org.com.openmarket.wallet.application.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDataDTO {
    private String event;
    private String extId;
    private String username;
    private String email;
}
