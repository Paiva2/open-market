package org.com.openmarket.items.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_base_attributes")
public class BaseAttributeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "bat_id")
    private UUID id;

    @Column(name = "bat_attributes", columnDefinition = "JSONB")
    @ColumnTransformer(write = "?::jsonb")
    private String attributes;

    @CreationTimestamp
    @Column(name = "bat_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "bat_updated_at")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bat_item_id")
    private ItemEntity item;
}
