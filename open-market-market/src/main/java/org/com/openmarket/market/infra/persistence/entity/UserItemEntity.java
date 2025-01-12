package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users_items")
public class UserItemEntity {
    @EmbeddedId
    private KeyId id;

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

    @Data
    @Builder
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyId {
        @Column(name = "uit_user_id")
        private UUID userId;

        @Column(name = "uit_item_id")
        private UUID itemId;
    }
}
