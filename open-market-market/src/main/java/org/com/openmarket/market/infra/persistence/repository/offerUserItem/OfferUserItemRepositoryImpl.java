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
    public List<OfferUserItem> findAllByItemSale(UUID itemSaleId) {
        List<OfferUserItemEntity> offerUserItemEntities = repository.findAllByItemSale(itemSaleId);
        return offerUserItemEntities.stream().map(OfferUserItemMapper::toDomain).toList();
    }

    @Override
    public Optional<OfferUserItem> findByUserItemAndAttribute(UUID userId, UUID itemId, UUID attributeId) {
        Optional<OfferUserItemEntity> offerUserItem = repository.findByUserItemAndAttribute(userId, itemId, attributeId);
        if (offerUserItem.isEmpty()) return Optional.empty();
        return Optional.of(OfferUserItemMapper.toDomain(offerUserItem.get()));
    }

    @Override
    public List<OfferUserItem> findByOfferIdWithDeps(UUID offerId) {
        List<OfferUserItemEntity> offerUserItems = repository.findAllByOfferIdWithUserItem(offerId);
        return offerUserItems.stream().map(OfferUserItemMapper::toDomain).toList();
    }

    @Override
    public void removeAll(List<OfferUserItem> offerUserItems) {
        repository.removeAll(offerUserItems.stream().map(OfferUserItemMapper::toPersistence).map(OfferUserItemEntity::getId).toList());
    }
}
