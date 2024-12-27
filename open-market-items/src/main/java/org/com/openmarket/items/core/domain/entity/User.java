package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private UUID id;
    private String externalId;
    private String userName;
    private String email;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;

    private List<ItemAlteration> itemAlterations;

    private List<UserItem> items;
}