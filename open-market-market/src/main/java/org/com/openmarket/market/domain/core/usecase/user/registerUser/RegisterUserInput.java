package org.com.openmarket.market.domain.core.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserInput {
    private String externalId;
    private String userName;
    private String email;

    public static RegisterUserInput toInput(String externalId, String userName, String email) {
        return RegisterUserInput.builder()
            .externalId(externalId)
            .userName(userName)
            .email(email)
            .build();
    }
}
