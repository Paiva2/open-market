package org.com.openmarket.market.infra.persistence.repository.offerUserItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.OfferUserItem;
import org.com.openmarket.market.domain.interfaces.OfferUserItemRepository;
import org.com.openmarket.market.infra.persistence.entity.OfferUserItemEntity;
import org.com.openmarket.market.infra.persistence.mapper.OfferUserItemMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OfferUserItemRepositoryImpl implements OfferUserItemRepository {
    private final OfferUserItemRepositoryOrm repository;

    @Override
    public void removeOfferUserItemByOfferIds(List<UUID> offerIds) {
        repository.removeAllByOfferIds(offerIds);
    }

    @Override
    public List<OfferUserItem> persistAll(List<OfferUserItem> offerUserItems) {
        List<OfferUserItemEntity> offerUserItemEntities = offerUserItems.stream().map(OfferUserItemMapper::toPersistence).toList();
        List<OfferUserItemEntity> offerUserItemSaved = repository.saveAll(offerUserItemEntities);
        return offerUserItemSaved.stream().map(OfferUserItemMapper::toDomain).toList();
    }

    @Override
    public Optional<OfferUserItem> findByUserItemAndAttribute(UUID userId, UUID itemId, UUID attributeId) {
        Optional<OfferUserItemEntity> offerUserItem = repository.findByUserItemAndAttribute(userId, itemId, attributeId);
        if (offerUserItem.isEmpty()) return Optional.empty();
        return Optional.of(OfferUserItemMapper.toDomain(offerUserItem.get()));
    }
}
