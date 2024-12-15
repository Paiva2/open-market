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
    private String externalId;
    private String userName;
    private String email;
}
