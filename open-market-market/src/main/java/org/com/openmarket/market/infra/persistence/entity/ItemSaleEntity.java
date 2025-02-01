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
@Table(name = "tb_items_sales")
public class ItemSaleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "isl_id")
    private UUID id;

    @Column(name = "isl_quantity")
    private Long quantity;

    @Column(name = "isl_value")
    private BigDecimal value;

    @Column(name = "isl_expiration_date")
    private Date expirationDate;

    @Column(name = "isl_accept_offers")
    private Boolean acceptOffers;

    @Column(name = "isl_only_offers")
    private Boolean onlyOffers;

    @CreationTimestamp
    @Column(name = "isl_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "isl_updated_at")
    private Date updatedAt;

    @JoinColumns({
        @JoinColumn(name = "isl_user_id", referencedColumnName = "uit_user_id"),
        @JoinColumn(name = "isl_item_id", referencedColumnName = "uit_item_id"),
        @JoinColumn(name = "isl_attribute_item_id", referencedColumnName = "uit_item_attribute_id"),
    })
    @ManyToOne(fetch = FetchType.LAZY)
    private UserItemEntity userItem;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "itemSale")
    private List<OfferEntity> offers;
}
