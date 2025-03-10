package org.com.openmarket.wallet.infra.persistence.entity;

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
@Table(name = "tb_wallets")
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wlt_id")
    private UUID id;

    @CreationTimestamp
    @Column(name = "wlt_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "wlt_updated_at")
    private Date updatedAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wlt_user_id")
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wallet")
    private List<WalletLedgerEntity> walletLedgers;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "targetWallet")
    private List<WalletLedgerEntity> walletTargetLedgers;
}
