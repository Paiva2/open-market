package org.com.openmarket.items.infra.persistence.repository.item;

import org.com.openmarket.items.infra.persistence.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ItemRepositoryOrm extends JpaRepository<ItemEntity, UUID> {
    Optional<ItemEntity> findByName(String name);
}
