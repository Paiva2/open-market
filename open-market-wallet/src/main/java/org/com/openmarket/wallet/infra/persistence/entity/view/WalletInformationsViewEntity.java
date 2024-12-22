package org.com.openmarket.wallet.infra.persistence.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.jcip.annotations.Immutable;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity
@Immutable
@Table(name = "wallet_informations_view")
public class WalletInformationsViewEntity implements Serializable {
    @Id
    private UUID id;

    @Column(name = "last_update")
    private Date lastUpdate;

    @Column(name = "current_balance")
    private Integer currentBalance;
}
