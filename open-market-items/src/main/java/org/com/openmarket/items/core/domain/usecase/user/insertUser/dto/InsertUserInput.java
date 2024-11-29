package org.com.openmarket.items.core.domain.usecase.user.insertUser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.application.gateway.message.userQueue.dto.UserDataMessageInput;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InsertUserInput {
    private String extId;
    private String username;
    private String email;

    public static InsertUserInput toUsecase(UserDataMessageInput input) {
        return InsertUserInput.builder()
            .extId(input.getExtId())
            .email(input.getEmail())
            .username(input.getUsername())
            .build();
    }
}
