package org.com.openmarket.items.application.gateway.message.userQueue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.enumeration.EnumUserEvents;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDataMessageInput {
    private EnumUserEvents event;
    private String extId;
    private String username;
    private String email;
}
