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
    private String extId;
    private String username;
    private String email;
    private String password;
}
