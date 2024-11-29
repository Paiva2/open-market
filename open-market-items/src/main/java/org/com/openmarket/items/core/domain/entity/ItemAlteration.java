package org.com.openmarket.items.core.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.com.openmarket.items.core.domain.enumeration.EnumItemAlteration;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ItemAlteration {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
    private EnumItemAlteration action;

    private User user;
    private Item item;
}
