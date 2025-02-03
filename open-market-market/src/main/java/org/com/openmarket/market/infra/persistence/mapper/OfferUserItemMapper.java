package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.OfferUserItem;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.com.openmarket.market.infra.persistence.entity.OfferUserItemEntity;
import org.springframework.beans.BeanUtils;

public class OfferUserItemMapper {
    public static OfferUserItem toDomain(OfferUserItemEntity persistenceEntity) {
        if (persistenceEntity == null) return null;
        OfferUserItem offerUserItem = new OfferUserItem();
        copyProperties(persistenceEntity, offerUserItem);

        if (persistenceEntity.getUserItem() != null) {
            offerUserItem.setUserItem(UserItemMapper.toDomain(persistenceEntity.getUserItem()));
        }

        if (persistenceEntity.getOffer() != null) {
            Offer offer = new Offer();
            copyProperties(persistenceEntity.getOffer(), offer);
            offerUserItem.setOffer(offer);
        }

        return offerUserItem;
    }

    public static OfferUserItemEntity toPersistence(OfferUserItem persistenceEntity) {
        if (persistenceEntity == null) return null;
        OfferUserItemEntity offerUserItem = new OfferUserItemEntity();
        copyProperties(persistenceEntity, offerUserItem);

        if (persistenceEntity.getUserItem() != null) {
            offerUserItem.setUserItem(UserItemMapper.toPersistence(persistenceEntity.getUserItem()));
        }

        if (persistenceEntity.getOffer() != null) {
            OfferEntity offer = new OfferEntity();
            copyProperties(persistenceEntity.getOffer(), offer);
            offerUserItem.setOffer(offer);
        }

        return offerUserItem;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
