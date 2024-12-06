package org.com.openmarket.items.core.domain.usecase.item.listItems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
public class ListItemsInput {
    private int page;
    private int size;
    private Long category;
    private BigDecimal maxPrice;
    private BigDecimal minPrice;
    private Boolean unique;
    private Boolean active;
    private String name;
    private String direction;
}
