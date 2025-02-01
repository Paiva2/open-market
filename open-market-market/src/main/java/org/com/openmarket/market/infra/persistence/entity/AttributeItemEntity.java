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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_attributes_item")
public class AttributeItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "aui_id")
    private UUID id;

    @Column(name = "aui_attributes", columnDefinition = "JSONB")
    private String attributes;

    @CreationTimestamp
    @Column(name = "aui_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "aui_updated_at")
    private Date updatedAt;
}