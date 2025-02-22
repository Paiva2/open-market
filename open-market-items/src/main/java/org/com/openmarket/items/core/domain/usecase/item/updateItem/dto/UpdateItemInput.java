package org.com.openmarket.items.core.domain.usecase.item.updateItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateItemInput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean active;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private String baseAttribute;
    private List<Long> categoriesIds;
}
