package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Category {
    private Long id;
    private String name;
    private Date createdAt;
    private Date updatedAt;
}