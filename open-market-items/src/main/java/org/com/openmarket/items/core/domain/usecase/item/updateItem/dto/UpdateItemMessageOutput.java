package org.com.openmarket.items.core.domain.usecase.item.updateItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemMessageOutput {
    private String id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private BaseAttributeOutput baseAttribute;
    private Boolean active;

    private List<String> categoriesIds;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseAttributeOutput {
        private String attributes;
    }
}
