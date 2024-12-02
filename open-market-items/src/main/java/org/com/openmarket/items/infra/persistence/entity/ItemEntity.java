package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "itm_id")
    private UUID id;

    @Column(name = "itm_name", unique = true, nullable = false)
    private String name;

    @Column(name = "itm_description", unique = false, nullable = false)
    private String description;

    @Column(name = "itm_photo_url", unique = false, nullable = false)
    private String photoUrl;

    @Column(name = "itm_unique", nullable = false)
    private Boolean unique;

    @Column(name = "itm_base_selling_price", nullable = false)
    private BigDecimal baseSellingPrice;

    @Column(name = "itm_active", nullable = false)
    private Boolean active;

    @CreationTimestamp
    @Column(name = "itm_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "itm_updated_at", nullable = false)
    private Date updatedAt;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemAlterationEntity> itemAlterations;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemCategoryEntity> itemCategories;
}
