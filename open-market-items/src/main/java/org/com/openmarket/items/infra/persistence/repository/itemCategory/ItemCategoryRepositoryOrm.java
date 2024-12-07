package org.com.openmarket.items.infra.persistence.repository.itemCategory;

import org.com.openmarket.items.infra.persistence.entity.ItemCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ItemCategoryRepositoryOrm extends JpaRepository<ItemCategoryEntity, ItemCategoryEntity.KeyId> {
    List<ItemCategoryEntity> findAllByItemId(UUID itemId);

    void deleteAllByItemId(UUID itemId);

    void deleteAllByCategoryId(Long categoryId);
}
