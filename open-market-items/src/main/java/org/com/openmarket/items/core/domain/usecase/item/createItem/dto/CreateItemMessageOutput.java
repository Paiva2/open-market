package org.com.openmarket.items.core.domain.usecase.item.createItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class CreateItemMessageOutput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private Boolean active;

    private List<Long> categoriesIds;
}
