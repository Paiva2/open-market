package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users_items")
public class UserItemEntity {
    @EmbeddedId
    private KeyId id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_attribute_id")
    private AttributeItemEntity attribute;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_user_id")
    private UserEntity user;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "uit_item_id")
    private ItemEntity item;

    @Column(name = "uit_quantity")
    private Long quantity;

    @Column(name = "uit_created_at")
    private Date createdAt;

    @Column(name = "uit_updated_at")
    private Date updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class KeyId {
        private UUID userId;
        private UUID itemId;
    }
}
