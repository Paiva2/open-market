package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.springframework.beans.BeanUtils;

public class OfferMapper {
    public static Offer toDomain(OfferEntity entity) {
        if (entity == null) return null;

        Offer offer = new Offer();
        copyProperties(entity, offer);

        return offer;
    }

    public static OfferEntity toPersistence(Offer entity) {
        if (entity == null) return null;

        OfferEntity offer = new OfferEntity();
        copyProperties(entity, offer);

        return offer;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
