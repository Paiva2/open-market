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
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "usr_id")
    private UUID id;

    @Column(name = "usr_external_id")
    private String externalId;

    @Column(name = "usr_username")
    private String userName;

    @Column(name = "usr_email")
    private String email;

    @Column(name = "usr_enabled")
    private Boolean enabled;

    @CreationTimestamp
    @Column(name = "usr_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "usr_updated_at")
    private Date updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<UserItemEntity> userItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<OfferEntity> offers;
}
