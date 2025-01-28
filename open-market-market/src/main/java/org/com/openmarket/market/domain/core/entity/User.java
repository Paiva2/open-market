package org.com.openmarket.market.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String externalId;
    private String userName;
    private String email;
    private Boolean enabled;
    private Date createdAt;
    private Date updatedAt;

    private List<Offer> offers;
    private List<OfferUserItem> offerUserItems;
}
