package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_offers_user_item")
public class OfferUserItemEntity {
    @EmbeddedId
    private KeyId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oui_user_id")
    private UserEntity user;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oui_item_id")
    private ItemEntity item;

    @MapsId("offerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oui_offer_id")
    private OfferEntity offer;

    @Column(name = "oui_quantity")
    private Long quantity;

    @CreationTimestamp
    @Column(name = "oui_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "oui_updated_at")
    private Date updatedAt;

    @Setter
    @Getter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyId {
        @JoinColumn(name = "oui_user_id")
        private UUID userId;

        @JoinColumn(name = "oui_item_id")
        private UUID itemId;

        @JoinColumn(name = "oui_offer_id")
        private UUID offerId;
    }
}
