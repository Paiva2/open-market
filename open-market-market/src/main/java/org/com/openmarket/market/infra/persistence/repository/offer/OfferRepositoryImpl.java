package org.com.openmarket.market.infra.persistence.repository.offer;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.interfaces.OfferRepository;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.com.openmarket.market.infra.persistence.mapper.OfferMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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

    @Override
    public PageableList<Offer> findAllByItemSale(UUID itemSaleId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.DESC, "createdAt");
        Page<OfferEntity> offerEntities = repository.findAllByItemSale(itemSaleId, pageable);

        return new PageableList<>(
            offerEntities.getNumber() + 1,
            offerEntities.getSize(),
            offerEntities.getTotalElements(),
            offerEntities.getTotalPages(),
            offerEntities.isLast(),
            offerEntities.stream().map(OfferMapper::toDomain).toList()
        );
    }

    @Override
    public Offer persist(Offer offer) {
        OfferEntity offerEntity = repository.save(OfferMapper.toPersistence(offer));
        return OfferMapper.toDomain(offerEntity);
    }

    @Override
    public Optional<Offer> findById(UUID id) {
        Optional<OfferEntity> offerEntity = repository.findByIdWithDeps(id);
        if (offerEntity.isEmpty()) return Optional.empty();
        return Optional.of(OfferMapper.toDomain(offerEntity.get()));
    }

    @Override
    public void delete(UUID offerId) {
        repository.deleteById(offerId);
    }
}
