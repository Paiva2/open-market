package org.com.openmarket.items.application.gateway.controller.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.dto.UpdateItemInput;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UpdateItemDTO {
    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String description;

    @NotEmpty
    @NotNull
    private String photoUrl;

    @NotNull
    private Boolean unique;

    @NotNull
    private BigDecimal baseSellingPrice;

    @NotNull
    @NotEmpty
    private List<@Min(0) Long> categoriesIds;

    public UpdateItemInput toInput(UUID itemId) {
        return UpdateItemInput.builder()
            .id(itemId)
            .name(this.name)
            .description(this.description)
            .photoUrl(this.photoUrl)
            .unique(this.unique)
            .baseSellingPrice(this.baseSellingPrice)
            .categoriesIds(this.categoriesIds)
            .build();
    }
}
