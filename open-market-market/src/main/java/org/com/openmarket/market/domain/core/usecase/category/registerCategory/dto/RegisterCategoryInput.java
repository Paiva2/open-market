package org.com.openmarket.market.domain.core.usecase.category.registerCategory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCategoryInput {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}
