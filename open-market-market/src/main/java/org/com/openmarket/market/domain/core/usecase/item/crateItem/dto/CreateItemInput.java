package org.com.openmarket.market.domain.core.usecase.item.crateItem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemInput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private Boolean active;
    private Boolean stackable;
    private Date createdAt;
    private Date updatedAt;

    private List<Long> categoriesIds;
}
