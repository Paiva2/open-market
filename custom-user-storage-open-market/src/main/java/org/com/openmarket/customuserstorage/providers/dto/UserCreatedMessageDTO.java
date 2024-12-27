package org.com.openmarket.customuserstorage.providers.dto;

import lombok.Data;
import org.com.openmarket.customuserstorage.providers.enumeration.EnumUserEvents;

@Data
public class UserCreatedMessageDTO {
    private EnumUserEvents event;
    private String extId;
    private String username;
    private String email;
    private String password;

    public UserCreatedMessageDTO(EnumUserEvents event, String extId, String username, String email, String password) {
        this.event = event;
        this.extId = extId;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserCreatedMessageDTO(EnumUserEvents event, String extId, String username, String email) {
        this.event = event;
        this.extId = extId;
        this.username = username;
        this.email = email;
    }
}