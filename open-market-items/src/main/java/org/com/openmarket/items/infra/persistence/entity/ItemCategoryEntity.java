package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_items_categories")
public class ItemCategoryEntity {
    @EmbeddedId
    private KeyId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ict_item_id")
    @MapsId("itemId")
    private ItemEntity item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ict_category_id")
    @MapsId("categoryId")
    private CategoryEntity category;

    @CreationTimestamp
    @Column(name = "ict_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ict_updated_at", nullable = false)
    private Date updatedAt;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Embeddable
    public static class KeyId {
        @Column(name = "ict_item_id")
        private UUID itemId;

        @Column(name = "ict_category_id")
        private Long categoryId;
    }
}
