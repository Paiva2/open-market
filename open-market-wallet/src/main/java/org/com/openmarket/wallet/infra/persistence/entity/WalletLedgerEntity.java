package org.com.openmarket.wallet.infra.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.com.openmarket.wallet.core.domain.enums.EnumTransactionType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_wallets_ledgers")
public class WalletLedgerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "wlg_id")
    private UUID id;

    @Column(name = "wlg_transaction_type")
    @Enumerated(EnumType.STRING)
    private EnumTransactionType transactionType;

    @Column(name = "wlg_value")
    private BigDecimal value;

    @Column(name = "wlg_description")
    private String description;

    @CreationTimestamp
    @Column(name = "wlg_created_at")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "wlg_updated_at")
    private Date updatedAt;

    @JoinColumn(name = "wlg_wallet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WalletEntity wallet;

    @JoinColumn(name = "wlg_target_wallet_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private WalletEntity targetWallet;
}
