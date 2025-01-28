package org.com.openmarket.market.infra.persistence.repository.offerUserItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.interfaces.OfferUserItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class OfferUserItemRepositoryImpl implements OfferUserItemRepository {
    private final OfferUserItemRepositoryOrm repository;

    @Override
    public void removeOfferUserItemByOfferIds(List<UUID> offerIds) {
        repository.removeAllByOfferIds(offerIds);
    }
}
