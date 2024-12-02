package org.com.openmarket.items.infra.persistence.repository.itemAlteration;

import org.com.openmarket.items.infra.persistence.entity.ItemAlterationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemAlterationRepositoryOrm extends JpaRepository<ItemAlterationEntity, Long> {
}
