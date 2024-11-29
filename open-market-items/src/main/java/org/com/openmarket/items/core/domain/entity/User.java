package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private long id;
    private String userName;
    private String email;
    private String password;

    private List<UserItem> items;
}