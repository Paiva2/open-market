package org.com.openmarket.market.infra.persistence.repository.baseAttribute;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.BaseAttribute;
import org.com.openmarket.market.domain.interfaces.BaseAttributeRepository;
import org.com.openmarket.market.infra.persistence.entity.BaseAttributeEntity;
import org.com.openmarket.market.infra.persistence.mapper.BaseAttributeMapper;
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
