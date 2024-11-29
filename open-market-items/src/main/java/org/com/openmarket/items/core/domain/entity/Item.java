package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Item {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private BigDecimal baseSellingPrice;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;

    private List<ItemCategory> categories;

    private List<UserItem> userItems;
    private List<ItemAlteration> itemAlterations;
}
