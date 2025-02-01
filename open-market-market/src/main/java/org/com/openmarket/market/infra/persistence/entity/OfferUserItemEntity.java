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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_offers_user_item")
public class OfferUserItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "oui_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "oui_user_id", referencedColumnName = "uit_user_id"),
        @JoinColumn(name = "oui_item_id", referencedColumnName = "uit_item_id"),
        @JoinColumn(name = "oui_attribute_item_id", referencedColumnName = "uit_item_attribute_id")
    })
    private UserItemEntity userItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oui_offer_id")
    private OfferEntity offer;

    @Column(name = "oui_quantity")
    private Long quantity;

    @CreationTimestamp
    @Column(name = "oui_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "oui_updated_at")
    private Date updatedAt;
}
