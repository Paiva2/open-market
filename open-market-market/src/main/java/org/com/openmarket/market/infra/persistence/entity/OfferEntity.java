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
@Table(name = "tb_offers")
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ofr_id")
    private UUID id;

    @Column(name = "ofr_value")
    private BigDecimal value;

    @CreationTimestamp
    @Column(name = "ofr_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "ofr_updated_at")
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ofr_user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ofr_item_sale_id")
    private ItemSaleEntity itemSale;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "offer")
    private List<OfferUserItemEntity> offerUserItems;
}
