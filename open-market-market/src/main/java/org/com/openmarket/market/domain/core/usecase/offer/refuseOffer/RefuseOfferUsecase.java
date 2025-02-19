package org.com.openmarket.market.domain.core.usecase.offer.refuseOffer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.*;
import org.com.openmarket.market.domain.core.usecase.offer.refuseOffer.exception.InvalidOfferPermissionException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;

@Service
@Slf4j
@AllArgsConstructor
public class RefuseOfferUsecase {
    private final static String BANK_ADMIN_EXT_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void execute(String externalUserId, UUID offerId) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Offer offer = findOffer(offerId);
        ItemSale itemSale = offer.getItemSale();
        getItemSaleItem(itemSale);

        checkItemSaleConstraints(itemSale, user);

        removeOfferUserItems(offer);
        removeOffer(offer);

        List<CommonMessageDTO> messages = new ArrayList<>();

        if (offer.getValue().compareTo(BigDecimal.ZERO) > 0) {
            chargeBackOfferValues(offer, messages);
        }

        if (offer.getOfferUserItems() != null && !offer.getOfferUserItems().isEmpty()) {
            restoreOfferedItemsQuantity(offer.getOfferUserItems(), messages);
        }

        sendMessages(messages);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Offer findOffer(UUID offerId) {
        return offerRepository.findById(offerId).orElseThrow(OfferNotFoundException::new);
    }

    private void getItemSaleItem(ItemSale itemSale) {
        Optional<UserItem> userItem = userItemRepository.findByItemSaleId(itemSale.getId());

        if (userItem.isEmpty()) {
            throw new UserItemNotFoundException();
        }

        itemSale.setUserItem(userItem.get());
    }

    private void checkItemSaleConstraints(ItemSale itemSale, User user) {
        if (itemSale.getExpirationDate().before(new Date())) {
            throw new ItemSaleNotFoundException();
        }

        if (!itemSale.getUserItem().getUser().getId().equals(user.getId())) {
            throw new InvalidOfferPermissionException();
        }
    }

    private void removeOfferUserItems(Offer offer) {
        if (offer.getOfferUserItems() != null && !offer.getOfferUserItems().isEmpty()) {
            offerUserItemRepository.removeAll(offer.getOfferUserItems());
        }
    }

    private void removeOffer(Offer offer) {
        offerRepository.delete(offer.getId());
    }

    private void chargeBackOfferValues(Offer offer, List<CommonMessageDTO> messages) {
        try {
            WalletMessageInput walletMessageInput = WalletMessageInput.builder()
                .externalUserId(System.getenv(BANK_ADMIN_EXT_ID))
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .externalUserTargetId(offer.getUser().getExternalId())
                    .description("Offer value chargeback: " + offer.getId())
                    .value(offer.getValue())
                    .type(EnumTransactionType.TRANSFER.name())
                    .build()
                ).build();

            CommonMessageDTO commonMessageDTO = mountCommonMessage(EnumMessageType.NEW_TRANSACTION, EnumMessageEvent.WALLET_EVENT, walletMessageInput);

            messages.add(commonMessageDTO);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreOfferedItemsQuantity(List<OfferUserItem> offerUserItems, List<CommonMessageDTO> messages) {
        List<UserItem> userItemsToUpdate = new ArrayList<>();

        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItem = offerUserItem.getUserItem();
            Long quantityOffered = offerUserItem.getQuantity();

            userItem.setQuantity(userItem.getQuantity() + quantityOffered);
            userItemsToUpdate.add(userItem);

            try {
                UpdateUserItemMessageInput updateInput = userItemUpdateMessage(userItem.getUser().getExternalId(), userItem.getItem().getExternalId(), userItem.getAttribute().getExternalId(), userItem.getQuantity());
                CommonMessageDTO commonMessageDTO = mountCommonMessage(EnumMessageType.UPDATED, EnumMessageEvent.USER_ITEM_EVENT, updateInput);

                messages.add(commonMessageDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        userItemRepository.persistAll(userItemsToUpdate);
    }

    private UpdateUserItemMessageInput userItemUpdateMessage(String extUserId, String extItemId, String extAttributeId, Long newQuantity) {
        return UpdateUserItemMessageInput.builder()
            .externalUserId(extUserId)
            .externalItemId(extItemId)
            .externalAttributeId(extAttributeId)
            .userItemInput(new UpdateUserItemMessageInput.UserItemInput(newQuantity))
            .build();
    }

    private CommonMessageDTO mountCommonMessage(EnumMessageType type, EnumMessageEvent event, Object data) throws JsonProcessingException {
        return CommonMessageDTO.builder()
            .type(type)
            .event(event)
            .data(mapper.writeValueAsString(data))
            .build();
    }

    private void sendMessages(List<CommonMessageDTO> messages) {
        for (CommonMessageDTO commonMessageDTO : messages) {
            try {
                String queue = commonMessageDTO.getEvent().equals(EnumMessageEvent.WALLET_EVENT) ? WALLET_QUEUE : ITEM_QUEUE;
                messageRepository.sendMessage(queue, mapper.writeValueAsString(commonMessageDTO));
            } catch (Exception e) {
                log.error("Error while sending message: {}", e.getMessage());
            }
        }
    }
}
