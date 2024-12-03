package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.openmarket.items.core.domain.enumeration.EnumItemAlteration;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_items_alterations")
@SequenceGenerator(name = "items_alterations_seq", sequenceName = "tb_items_alterations_ial_id_seq", allocationSize = 1)
public class ItemAlterationEntity {
    @Id
    @Column(name = "ial_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "items_alterations_seq")
    private Long id;

    @Column(name = "ial_action", nullable = false)
    @Enumerated(EnumType.STRING)
    private EnumItemAlteration action;

    @CreationTimestamp
    @Column(name = "ial_created_at", nullable = false, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ial_updated_at", nullable = false)
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ial_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ial_item_id")
    private ItemEntity item;
}
