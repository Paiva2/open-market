package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_categories")
@SequenceGenerator(name = "categories_seq", sequenceName = "tb_categories_cat_id_seq", allocationSize = 1)
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "categories_seq")
    @Column(name = "cat_id")
    private Long id;

    @Column(name = "cat_name", nullable = false, unique = true, length = 50)
    private String name;

    @CreationTimestamp
    @Column(name = "cat_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "cat_updated_at", nullable = false)
    private Date updatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<ItemCategoryEntity> itemCategories;
}
