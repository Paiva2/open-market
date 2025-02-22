package org.com.openmarket.market.infra.persistence.entity;

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

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_items")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "itm_id")
    private UUID id;

    @Column(name = "itm_external_id", unique = true)
    private String externalId;

    @Column(name = "itm_name", unique = true)
    private String name;

    @Column(name = "itm_description")
    private String description;

    @Column(name = "itm_photo_url")
    private String photoUrl;

    @Column(name = "itm_unique")
    private Boolean unique;

    @Column(name = "itm_base_selling_price")
    private BigDecimal baseSellingPrice;

    @Column(name = "itm_active")
    private Boolean active;

    @Column(name = "itm_stackable")
    private Boolean stackable;

    @CreationTimestamp
    @Column(name = "itm_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "itm_updated_at")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "item")
    private BaseAttributeEntity baseAttribute;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private List<ItemCategoryEntity> itemCategories;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "item")
    private List<UserItemEntity> userItems;
}
