package org.com.openmarket.wallet.application.gateway.controller.messages.userDataMessages.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMessageDTO {
    private String event;
    private String extId;
    private String username;
    private String email;
}
