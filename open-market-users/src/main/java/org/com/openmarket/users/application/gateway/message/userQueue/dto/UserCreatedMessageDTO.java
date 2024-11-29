package org.com.openmarket.users.application.gateway.message.userQueue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserCreatedMessageDTO {
    private String event;
    private String extId;
    private String username;
    private String email;
}