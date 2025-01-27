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
@Table(name = "tb_items_categories")
public class ItemCategoryEntity {
    @EmbeddedId
    private KeyId id;

    @MapsId("itemId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ict_item_id")
    private ItemEntity item;

    @MapsId("categoryId")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ict_category_id")
    private CategoryEntity category;

    @CreationTimestamp
    @Column(name = "ict_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ict_updated_at", nullable = false)
    private Date updatedAt;

    @Setter
    @Getter
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KeyId {
        @Column(name = "ict_item_id")
        private UUID itemId;

        @Column(name = "ict_category_id")
        private Long categoryId;
    }
}
