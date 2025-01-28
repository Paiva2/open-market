package org.com.openmarket.market.infra.persistence.repository.offer;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.interfaces.OfferRepository;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.com.openmarket.market.infra.persistence.mapper.OfferMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OfferRepositoryImpl implements OfferRepository {
    private final OfferRepositoryOrm repository;

    @Override
    public List<Offer> getItemSaleOffers(UUID itemSaleId) {
        List<OfferEntity> offerEntities = repository.findAllByItemSaleId(itemSaleId);
        return offerEntities.stream().map(OfferMapper::toDomain).toList();
    }

    @Override
    public void removeOffers(List<Offer> offers) {
        List<OfferEntity> offersEntities = offers.stream().map(OfferMapper::toPersistence).toList();
        repository.deleteAll(offersEntities);
    }
}
