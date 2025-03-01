package org.com.openmarket.items.core.domain.usecase.item.findItem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindItemOutput {
    private UUID id;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private Boolean stackable;
    private BigDecimal baseSellingPrice;
    private Boolean active;
    private BaseAttributeOutput baseAttribute;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaseAttributeOutput {
        private UUID id;
        private String attributes;
    }
}
