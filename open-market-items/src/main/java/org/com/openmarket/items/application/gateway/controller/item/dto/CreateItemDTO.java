package org.com.openmarket.items.application.gateway.controller.item.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemInput;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateItemDTO {
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
    private Boolean stackable;

    @NotNull
    private BigDecimal baseSellingPrice;

    @NotNull
    private List<Long> categoriesIds;

    public CreateItemInput toInput() {
        return CreateItemInput.builder()
            .name(this.name)
            .description(this.description)
            .photoUrl(this.photoUrl)
            .unique(this.unique)
            .baseSellingPrice(this.baseSellingPrice)
            .categoriesIds(this.categoriesIds)
            .stackable(this.stackable)
            .build();
    }
}
