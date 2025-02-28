package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.infra.persistence.entity.ItemSaleEntity;
import org.com.openmarket.market.infra.persistence.entity.OfferEntity;
import org.com.openmarket.market.infra.persistence.entity.OfferUserItemEntity;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class OfferMapper {
    public static Offer toDomain(OfferEntity entity) {
        if (entity == null) return null;

        Offer offer = new Offer();
        copyProperties(entity, offer);

        if (entity.getUser() != null) {
            offer.setUser(UserMapper.toDomain(entity.getUser()));
        }

        if (entity.getItemSale() != null) {
            ItemSale itemSale = new ItemSale();
            copyProperties(entity.getItemSale(), itemSale);
            offer.setItemSale(itemSale);


            if (entity.getItemSale().getUserItem() != null) {
                offer.getItemSale().setUserItem(UserItemMapper.toDomain(entity.getItemSale().getUserItem()));
            }
        }

        if (entity.getOfferUserItems() != null && !entity.getOfferUserItems().isEmpty()) {
            List<OfferUserItem> offerUserItems = new ArrayList<>();

            for (OfferUserItemEntity offerUserItemEntity : entity.getOfferUserItems()) {
                OfferUserItem offerUserItem = new OfferUserItem();
                copyProperties(offerUserItemEntity, offerUserItem);

                if (offerUserItemEntity.getUserItem() != null) {
                    UserItem userItem = new UserItem();
                    copyProperties(offerUserItemEntity.getUserItem(), userItem);

                    if (offerUserItemEntity.getUserItem().getId() != null) {
                        UserItem.KeyId keyId = new UserItem.KeyId();
                        copyProperties(offerUserItemEntity.getUserItem().getId(), keyId);
                        userItem.setId(keyId);
                    }

                    if (offerUserItemEntity.getUserItem().getUser() != null) {
                        User user = new User();
                        BeanUtils.copyProperties(offerUserItemEntity.getUserItem().getUser(), user);
                        userItem.setUser(user);
                    }

                    if (offerUserItemEntity.getUserItem().getItem() != null) {
                        userItem.setItem(ItemMapper.toDomain(offerUserItemEntity.getUserItem().getItem()));
                    }

                    if (offerUserItemEntity.getUserItem().getAttribute() != null) {
                        AttributeItem attributeItem = new AttributeItem();
                        copyProperties(offerUserItemEntity.getUserItem().getAttribute(), attributeItem);
                        userItem.setAttribute(attributeItem);
                    }

                    offerUserItem.setUserItem(userItem);
                }

                offerUserItems.add(offerUserItem);
            }

            offer.setOfferUserItems(offerUserItems);
        }

        return offer;
    }

    public static OfferEntity toPersistence(Offer entity) {
        if (entity == null) return null;

        OfferEntity offer = new OfferEntity();
        copyProperties(entity, offer);

        if (entity.getUser() != null) {
            UserEntity user = new UserEntity();
            copyProperties(entity.getUser(), user);
            offer.setUser(user);
        }

        if (entity.getItemSale() != null) {
            ItemSaleEntity itemSaleEntity = new ItemSaleEntity();
            copyProperties(entity.getItemSale(), itemSaleEntity);
            offer.setItemSale(itemSaleEntity);
        }

        return offer;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
