package org.com.openmarket.wallet.core.domain.usecase.user.registerUser.dto;

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
