package org.com.openmarket.items.core.domain.usecase.item.listItems.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ListItemsInput {
    private int page;
    private int size;
    private Long category;
    private Boolean active;
    private String name;
    private String direction;
}
