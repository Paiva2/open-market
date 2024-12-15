package org.com.openmarket.wallet.application.gateway.controller.messages.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMessageDTO {
    private String event;
    private String extId;
    private String username;
    private String email;
}
