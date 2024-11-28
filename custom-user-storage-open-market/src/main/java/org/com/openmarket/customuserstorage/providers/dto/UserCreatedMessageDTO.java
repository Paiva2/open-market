package org.com.openmarket.customuserstorage.providers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.com.openmarket.customuserstorage.providers.enumeration.EnumUserEvents;

@AllArgsConstructor
@Data
public class UserCreatedMessageDTO {
    private final EnumUserEvents event;
    private final String extId;
    private final String username;
    private final String email;
}