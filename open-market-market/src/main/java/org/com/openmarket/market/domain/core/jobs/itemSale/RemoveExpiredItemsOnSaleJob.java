package org.com.openmarket.market.domain.core.jobs.itemSale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;
import static org.com.openmarket.market.domain.enumeration.EnumMessageEvent.WALLET_EVENT;

@Slf4j
@Component
@RequiredArgsConstructor
public class RemoveExpiredItemsOnSaleJob {
    @Value("${open-market.bank-admin-external-id}")
    private String BANK_ADMIN_EXTERNAL_ID;
    private final static BigDecimal MARKET_TAX = new BigDecimal("0.02"); // 2%
    private final static ObjectMapper mapper = new ObjectMapper();

    private final ItemSaleRepository itemSaleRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;

    @Scheduled(cron = "0 0 0 * * *") // midnight
    public void execute() {
        log.info("Executing RemoveExpiredItemsOnSaleJob");

        List<ItemSale> expiredItemsOnSale = findExpiredSales();

        if (expiredItemsOnSale.isEmpty()) return;

        List<UserItem> userItemsUpdated = new ArrayList<>();
        List<CommonMessageDTO> commonMessageDTOS = new ArrayList<>();

        for (ItemSale itemSale : expiredItemsOnSale) {
            UserItem userItemOnSale = itemSale.getUserItem();
            User user = userItemOnSale.getUser();

            Long updatedUserItemQty = userItemOnSale.getQuantity() + itemSale.getQuantity();

            try {
                if (!itemSale.getOffers().isEmpty()) {
                    removeItemSaleOffers(itemSale.getOffers(), userItemsUpdated, commonMessageDTOS);
                    itemSale.setOffers(null);
                }

                userItemOnSale.setQuantity(updatedUserItemQty);

                fillChargebackUserItemQuantity(commonMessageDTOS, user, userItemOnSale.getItem(), userItemOnSale.getAttribute(), userItemOnSale.getQuantity());
                fillChargeBackUserWalletValue(commonMessageDTOS, BANK_ADMIN_EXTERNAL_ID, user.getExternalId(), getTaxValueToChargeback(itemSale.getValue(), itemSale.getQuantity()), "Tax retrieval.");
            } catch (Exception e) {
                log.error("Error on removing expired items sales job {}", e.getMessage());
                throw new RuntimeException(e);
            }

            userItemsUpdated.add(userItemOnSale);
        }

        userItemRepository.persistAll(userItemsUpdated);
        itemSaleRepository.deleteAll(expiredItemsOnSale);

        sendAllMessages(commonMessageDTOS);
    }

    private List<ItemSale> findExpiredSales() {
        return itemSaleRepository.findAllExpired();
    }

    private void removeItemSaleOffers(List<Offer> offersToRemove, List<UserItem> userItemsUpdated, List<CommonMessageDTO> commonMessageDTOS) throws JsonProcessingException {
        for (Offer offer : offersToRemove) {
            List<OfferUserItem> offerUserItems = findOfferUserItems(offer.getId());
            User user = offer.getUser();

            if (!offerUserItems.isEmpty()) {
                removeOfferUsersItems(offerUserItems, userItemsUpdated, commonMessageDTOS, user);
                offerUserItemRepository.removeAll(offerUserItems);
            }

            if (offer.getValue().compareTo(BigDecimal.ZERO) > 0) {
                String chargebackDescription = "Money chargeback from offer: " + offer.getId();
                fillChargeBackUserWalletValue(commonMessageDTOS, BANK_ADMIN_EXTERNAL_ID, user.getExternalId(), offer.getValue(), chargebackDescription);
            }
        }

        offerRepository.removeOffers(offersToRemove);
    }

    private void removeOfferUsersItems(List<OfferUserItem> offerUserItems, List<UserItem> userItemsUpdated, List<CommonMessageDTO> commonMessageDTOS, User user) throws JsonProcessingException {
        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItemOffered = offerUserItem.getUserItem();
            Item item = userItemOffered.getItem();
            AttributeItem attributeItem = userItemOffered.getAttribute();
            Long quantityOffered = offerUserItem.getQuantity();

            userItemOffered.setQuantity(userItemOffered.getQuantity() + quantityOffered);

            userItemsUpdated.add(userItemOffered);

            fillChargebackUserItemQuantity(commonMessageDTOS, user, item, attributeItem, userItemOffered.getQuantity());
        }
    }

    private CommonMessageDTO makeCommonMessageDTO(EnumMessageType type, EnumMessageEvent event, Object data) throws JsonProcessingException {
        return CommonMessageDTO.builder()
            .type(type)
            .event(event)
            .data(mapper.writeValueAsString(data))
            .build();
    }

    private void fillChargebackUserItemQuantity(List<CommonMessageDTO> commonMessageDTOS, User user, Item item, AttributeItem attributeItem, Long quantity) throws JsonProcessingException {
        UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
            .externalUserId(user.getExternalId())
            .externalItemId(item.getExternalId())
            .externalAttributeId(attributeItem.getExternalId())
            .userItemInput(new UpdateUserItemMessageInput.UserItemInput(quantity))
            .build();

        CommonMessageDTO commonMessageDTO = makeCommonMessageDTO(EnumMessageType.UPDATED, EnumMessageEvent.USER_ITEM_EVENT, updateUserItemMessageInput);
        commonMessageDTOS.add(commonMessageDTO);
    }

    private void fillChargeBackUserWalletValue(List<CommonMessageDTO> commonMessageDTOS, String externalBankAdminId, String externalUserId, BigDecimal value, String description) throws JsonProcessingException {
        WalletMessageInput walletMessageInput = WalletMessageInput.builder()
            .externalUserId(externalBankAdminId)
            .transaction(WalletMessageInput.NewTransaction.builder()
                .externalUserTargetId(externalUserId)
                .description(description)
                .value(value)
                .type(EnumTransactionType.DEPOSIT.name())
                .build()
            ).build();

        CommonMessageDTO commonMessageDTO = makeCommonMessageDTO(EnumMessageType.NEW_TRANSACTION, WALLET_EVENT, walletMessageInput);
        commonMessageDTOS.add(commonMessageDTO);
    }

    private BigDecimal getTaxValueToChargeback(BigDecimal value, Long quantity) {
        BigDecimal totalItemSaleValue = value.multiply(BigDecimal.valueOf(quantity));
        return totalItemSaleValue.multiply(MARKET_TAX);
    }

    private List<OfferUserItem> findOfferUserItems(UUID offerId) {
        return offerUserItemRepository.findByOfferIdWithDeps(offerId);
    }

    private void sendAllMessages(List<CommonMessageDTO> commonMessagesDtos) {
        for (CommonMessageDTO commonMessageDTO : commonMessagesDtos) {
            String queue = commonMessageDTO.getEvent().equals(WALLET_EVENT) ? WALLET_QUEUE : ITEM_QUEUE;

            try {
                messageRepository.sendMessage(queue, mapper.writeValueAsString(commonMessageDTO));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
