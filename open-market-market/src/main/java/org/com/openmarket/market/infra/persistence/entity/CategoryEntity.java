package org.com.openmarket.market.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_categories")
@SequenceGenerator(name = "categories_seq", sequenceName = "tb_categories_cat_id_seq", allocationSize = 1)
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "categories_seq")
    @Column(name = "cat_id")
    private Long id;

    @Column(name = "cat_external_id")
    private String externalId;

    @Column(name = "cat_name", unique = true)
    private String name;

    @CreationTimestamp
    @Column(name = "cat_created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "cat_updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ItemCategoryEntity> itemCategories;
}
