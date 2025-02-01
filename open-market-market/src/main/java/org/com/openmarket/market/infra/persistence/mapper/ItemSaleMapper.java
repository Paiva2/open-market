package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.Item;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.infra.persistence.entity.ItemEntity;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemSaleMapper {
    public static ItemSale toDomain(ItemSaleEntity entity) {
        if (entity == null) return null;

        ItemSale itemSale = new ItemSale();
        copyProperties(entity, itemSale);

        if (entity.getUserItem() != null) {
            if (entity.getUserItem().getItem() != null) {
                Item item = new Item();
                copyProperties(entity.getUserItem().getItem(), item);

                itemSale.getUserItem().setItem(item);
            }

            if (entity.getUserItem().getUser() != null) {
                User user = new User();
                copyProperties(entity.getUserItem().getUser(), user);

                itemSale.getUserItem().setUser(user);
            }
        }


        if (entity.getOffers() != null) {
            List<Offer> offers = new ArrayList<>();
            for (OfferEntity offerEntity : entity.getOffers()) {
                Offer offer = new Offer();
                copyProperties(offerEntity, offer);
                offers.add(offer);
            }

            itemSale.setOffers(offers);
        }

        return itemSale;
    }

    public static ItemSaleEntity toPersistence(ItemSale entity) {
        if (entity == null) return null;

        ItemSaleEntity itemSale = new ItemSaleEntity();
        copyProperties(entity, itemSale);

        if (itemSale.getUserItem() != null) {
            if (entity.getUserItem().getItem() != null) {
                ItemEntity item = new ItemEntity();
                copyProperties(entity.getUserItem().getItem(), item);

                itemSale.getUserItem().setItem(item);
            }

            if (entity.getUserItem().getUser() != null) {
                UserEntity user = new UserEntity();
                copyProperties(entity.getUserItem().getUser(), user);

                itemSale.getUserItem().setUser(user);
            }
        }


        if (entity.getOffers() != null) {
            List<OfferEntity> offersEntities = new ArrayList<>();

            for (Offer offer : entity.getOffers()) {
                OfferEntity offerEntity = new OfferEntity();
                copyProperties(offer, offerEntity);

                offersEntities.add(offerEntity);
            }

            itemSale.setOffers(offersEntities);
        }

        return itemSale;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
