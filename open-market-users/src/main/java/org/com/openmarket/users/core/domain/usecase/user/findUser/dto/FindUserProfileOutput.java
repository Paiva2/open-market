package org.com.openmarket.users.core.domain.usecase.user.findUser.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class FindUserProfileOutput {
    private long id;
    private String userName;
    private String email;
}
