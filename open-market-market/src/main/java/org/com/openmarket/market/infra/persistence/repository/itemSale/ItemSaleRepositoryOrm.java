package org.com.openmarket.market.infra.persistence.repository.itemSale;

import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemSaleRepositoryOrm extends JpaRepository<ItemSaleEntity, UUID> {
}
