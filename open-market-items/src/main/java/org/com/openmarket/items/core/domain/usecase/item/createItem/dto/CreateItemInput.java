package org.com.openmarket.items.core.domain.usecase.item.createItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateItemInput {
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private Boolean stackable;
    private BigDecimal baseSellingPrice;
    private List<Long> categoriesIds;
    private String baseAttribute;
}
