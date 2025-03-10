package org.com.openmarket.wallet.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String externalId;
    private String username;
    private String email;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;

    private Wallet wallet;
}
