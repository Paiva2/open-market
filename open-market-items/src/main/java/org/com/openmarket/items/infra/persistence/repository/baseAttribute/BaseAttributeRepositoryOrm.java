package org.com.openmarket.items.infra.persistence.repository.baseAttribute;

import org.com.openmarket.items.infra.persistence.entity.BaseAttributeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BaseAttributeRepositoryOrm extends JpaRepository<BaseAttributeEntity, UUID> {
}
