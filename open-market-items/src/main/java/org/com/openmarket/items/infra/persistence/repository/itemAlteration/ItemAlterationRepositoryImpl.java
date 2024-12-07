package org.com.openmarket.items.infra.persistence.repository.itemAlteration;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.ItemAlteration;
import org.com.openmarket.items.core.domain.interfaces.repository.ItemAlterationRepository;
import org.com.openmarket.items.infra.persistence.entity.ItemAlterationEntity;
import org.com.openmarket.items.infra.persistence.mapper.ItemAlterationMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ItemAlterationRepositoryImpl implements ItemAlterationRepository {
    private final ItemAlterationRepositoryOrm repository;

    @Override
    public ItemAlteration save(ItemAlteration itemAlteration) {
        ItemAlterationEntity entity = repository.save(ItemAlterationMapper.toPersistence(itemAlteration));
        return ItemAlterationMapper.toDomain(entity);
    }
}
