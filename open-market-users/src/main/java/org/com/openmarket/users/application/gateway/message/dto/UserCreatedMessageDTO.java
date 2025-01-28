package org.com.openmarket.users.application.gateway.message.dto;

import lombok.Data;

@Data
public class UserCreatedMessageDTO {
    private String extId;
    private String username;
    private String email;
    private String password;

    public UserCreatedMessageDTO(String extId, String username, String email, String password) {
        this.extId = extId;
        this.username = username;
        this.email = email;
        this.password = password;
    }
}