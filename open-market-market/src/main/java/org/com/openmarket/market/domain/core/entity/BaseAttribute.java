package org.com.openmarket.market.domain.core.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseAttribute {
    private UUID id;
    private String attributes;
    private String externalId;
    private Date createdAt;
    private Date updatedAt;

    private Item item;
}
