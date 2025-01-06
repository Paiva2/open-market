package org.com.openmarket.items.core.domain.usecase.user.insertUser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InsertUserInput {
    private String extId;
    private String username;
    private String email;
    private String password;
}
