package org.com.openmarket.items.infra.persistence.repository.baseAttribute;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.BaseAttribute;
import org.com.openmarket.items.core.domain.interfaces.repository.BaseAttributeRepository;
import org.com.openmarket.items.infra.persistence.entity.BaseAttributeEntity;
import org.com.openmarket.items.infra.persistence.mapper.BaseAttributeMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BaseAttributeRepositoryImpl implements BaseAttributeRepository {
    private final BaseAttributeRepositoryOrm repository;

    @Override
    public BaseAttribute save(BaseAttribute baseAttribute) {
        BaseAttributeEntity baseAttributeSaved = repository.save(BaseAttributeMapper.toPersistence(baseAttribute));
        return BaseAttributeMapper.toDomain(baseAttributeSaved);
    }
}
