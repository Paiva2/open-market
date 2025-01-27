package org.com.openmarket.market.infra.persistence.repository.itemCategory;

import org.com.openmarket.market.infra.persistence.entity.ItemCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;
import java.util.UUID;

public interface ItemCategoryRepositoryOrm extends JpaRepository<ItemCategoryEntity, ItemCategoryEntity.KeyId> {
    List<ItemCategoryEntity> findAllByItemId(UUID itemId);

    @Modifying
    void deleteAllByItemId(UUID itemId);
}
