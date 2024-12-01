package org.com.openmarket.items.infra.persistence.repository.itemCategory;

import org.com.openmarket.items.infra.persistence.entity.ItemCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemCategoryRepositoryOrm extends JpaRepository<ItemCategoryEntity, ItemCategoryEntity.KeyId> {
}
