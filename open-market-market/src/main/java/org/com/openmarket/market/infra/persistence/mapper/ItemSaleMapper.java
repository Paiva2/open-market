package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.infra.persistence.entity.*;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemSaleMapper {
    public static ItemSale toDomain(ItemSaleEntity entity) {
        if (entity == null) return null;

        ItemSale itemSale = new ItemSale();
        copyProperties(entity, itemSale);

        if (entity.getUserItem() != null) {
            UserItem userItem = new UserItem();
            itemSale.setUserItem(userItem);

            copyProperties(entity.getUserItem(), itemSale.getUserItem());

            if (entity.getUserItem().getItem() != null) {
                Item item = new Item();
                copyProperties(entity.getUserItem().getItem(), item);

                itemSale.getUserItem().setItem(item);

                if (entity.getUserItem().getItem().getBaseAttribute() != null) {
                    BaseAttribute baseAttribute = new BaseAttribute();
                    copyProperties(entity.getUserItem().getItem().getBaseAttribute(), baseAttribute);
                    itemSale.getUserItem().getItem().setBaseAttribute(baseAttribute);
                }
            }

            if (entity.getUserItem().getUser() != null) {
                User user = new User();
                copyProperties(entity.getUserItem().getUser(), user);

                itemSale.getUserItem().setUser(user);
            }

            if (entity.getUserItem().getAttribute() != null) {
                AttributeItem attributeItem = new AttributeItem();
                copyProperties(entity.getUserItem().getAttribute(), attributeItem);
                itemSale.getUserItem().setAttribute(attributeItem);
            }

            if (entity.getUserItem().getId() != null) {
                UserItem.KeyId id = new UserItem.KeyId();
                copyProperties(entity.getUserItem().getId(), id);
                itemSale.getUserItem().setId(id);
            }
        }

        if (entity.getOffers() != null) {
            List<Offer> offers = new ArrayList<>();
            for (OfferEntity offerEntity : entity.getOffers()) {
                Offer offer = new Offer();
                copyProperties(offerEntity, offer);

                if (offerEntity.getUser() != null) {
                    User user = new User();
                    copyProperties(offerEntity.getUser(), user);
                    offer.setUser(user);
                }

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

        if (entity.getUserItem() != null) {
            UserItemEntity userItem = new UserItemEntity();
            itemSale.setUserItem(userItem);

            copyProperties(entity.getUserItem(), userItem);

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

            if (entity.getUserItem().getAttribute() != null) {
                AttributeItemEntity attributeItem = new AttributeItemEntity();
                copyProperties(entity.getUserItem().getAttribute(), attributeItem);
                itemSale.getUserItem().setAttribute(attributeItem);
            }

            if (entity.getUserItem().getId() != null) {
                UserItemEntity.KeyId id = new UserItemEntity.KeyId();
                copyProperties(entity.getUserItem().getId(), id);
                itemSale.getUserItem().setId(id);
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
