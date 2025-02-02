package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users_items")
public class UserItemEntity {
    @EmbeddedId
    private KeyId id;

    @MapsId("attributeId")
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_item_attribute_id")
    private AttributeItemEntity attribute;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_item_id")
    private ItemEntity item;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_user_id")
    private UserEntity user;

    @Column(name = "uit_quantity")
    private Long quantity;

    @CreationTimestamp
    @Column(name = "uit_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "uit_updated_at", nullable = false)
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userItem")
    private List<ItemSaleEntity> itemSales;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userItem")
    private List<OfferUserItemEntity> offersUserItems;

    @Getter
    @Setter
    @Builder
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId implements Serializable {
        private UUID attributeId;
        private UUID userId;
        private UUID itemId;
    }
}
