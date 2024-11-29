package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Category {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;

    private List<ItemCategory> itemCategories;
}
