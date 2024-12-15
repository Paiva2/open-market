package org.com.openmarket.wallet.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_wallets")
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wlt_id")
    private UUID id;

    @CreationTimestamp
    @Column(name = "wlt_created_at")
    private String createdAt;

    @UpdateTimestamp
    @Column(name = "wlt_updated_at")
    private String updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wlt_user_id")
    private UserEntity user;
}
