package org.com.openmarket.items.application.gateway.message.userQueue.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.enumeration.EnumMessageType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDataMessageInput {
    private EnumMessageType event;
    private String extId;
    private String username;
    private String email;
}
