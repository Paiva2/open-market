package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private UUID id;
    private String externalId;
    private String name;
    private String description;
    private String photoUrl;
    private Boolean unique;
    private BigDecimal baseSellingPrice;
    private Boolean active;
    private Boolean stackable;
    private Date createdAt;
    private Date updatedAt;

    private BaseAttribute baseAttribute;
    private List<ItemCategory> itemCategories;
    private List<OfferUserItem> offerUserItems;
}
