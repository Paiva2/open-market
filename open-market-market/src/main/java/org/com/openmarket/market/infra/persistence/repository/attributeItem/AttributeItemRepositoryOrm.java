package org.com.openmarket.market.infra.persistence.repository.attributeItem;

import org.com.openmarket.market.infra.persistence.entity.AttributeItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AttributeItemRepositoryOrm extends JpaRepository<AttributeItemEntity, UUID> {
}
