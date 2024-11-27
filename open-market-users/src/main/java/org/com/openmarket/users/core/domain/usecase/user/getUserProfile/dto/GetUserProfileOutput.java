package org.com.openmarket.users.core.domain.usecase.user.getUserProfile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class GetUserProfileOutput {
    private long id;
    private String userName;
    private String email;
}
