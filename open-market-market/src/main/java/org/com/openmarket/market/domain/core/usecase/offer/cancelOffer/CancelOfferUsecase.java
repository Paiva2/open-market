package org.com.openmarket.market.domain.core.usecase.offer.cancelOffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.OfferUserItem;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.OfferNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;

@Service
@AllArgsConstructor
public class CancelOfferUsecase {
    private final static String SYSTEM_BANK_EXTERNAL_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;
    private final WalletRepository walletRepository;

    @Transactional
    public void execute(String externalUserId, UUID offerId) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Offer offer = findOffer(offerId);
        checkOfferUserPermission(user, offer);

        List<OfferUserItem> offerUserItems = checkOfferHasItem(offer);

        if (offer.getValue().compareTo(BigDecimal.ZERO) > 0) {
            String bankAdminExternalId = System.getenv(SYSTEM_BANK_EXTERNAL_ID);
            UserWalletViewOutput userWallet = findUserWallet();

            sendWalletOfferedValueBack(bankAdminExternalId, userWallet.getId(), offer);
        }

        if (!offerUserItems.isEmpty()) {
            restoreOfferedItemsQuantity(offerUserItems);
            removeOfferUserItems(offerUserItems);
        }

        deleteOffer(offer);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Offer findOffer(UUID offerId) {
        return offerRepository.findById(offerId).orElseThrow(OfferNotFoundException::new);
    }

    private void checkOfferUserPermission(User user, Offer offer) {
        if (!offer.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Offer do not belong to provided user!");
        }
    }

    private List<OfferUserItem> checkOfferHasItem(Offer offer) {
        return offerUserItemRepository.findByOfferIdWithDeps(offer.getId());
    }

    private UserWalletViewOutput findUserWallet() {
        return walletRepository.getUserWalletView();
    }

    private void sendWalletOfferedValueBack(String bankAdminId, UUID userWalletId, Offer offer) {
        try {
            WalletMessageInput walletMessageInput = WalletMessageInput.builder()
                .externalUserId(bankAdminId)
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .targetWalletId(userWalletId)
                    .description("Money chargeback from offer: " + offer.getId())
                    .value(offer.getValue())
                    .type(EnumTransactionType.TRANSFER.name())
                    .build()
                ).build();

            CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
                .event(EnumMessageEvent.WALLET_EVENT)
                .type(EnumMessageType.NEW_TRANSACTION)
                .data(mapper.writeValueAsString(walletMessageInput))
                .build();

            messageRepository.sendMessage(WALLET_QUEUE, mapper.writeValueAsString(commonMessageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void restoreOfferedItemsQuantity(List<OfferUserItem> offerUserItems) {
        List<UserItem> userItemsUpdated = new ArrayList<>();

        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItem = offerUserItem.getUserItem();
            Long quantityOffered = offerUserItem.getQuantity();
            Long currUserItemQuantity = userItem.getQuantity();

            userItem.setQuantity(currUserItemQuantity + quantityOffered);

            userItemsUpdated.add(userItem);

            sendMessageUpdateUserItem(userItem);
        }

        userItemRepository.persistAll(userItemsUpdated);
    }

    private void sendMessageUpdateUserItem(UserItem userItem) {
        try {
            UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
                .externalAttributeId(userItem.getAttribute().getExternalId())
                .externalUserId(userItem.getUser().getExternalId())
                .externalItemId(userItem.getItem().getExternalId())
                .userItemInput(new UpdateUserItemMessageInput.UserItemInput(userItem.getQuantity()))
                .build();

            CommonMessageDTO commonMessageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.UPDATED)
                .event(EnumMessageEvent.USER_ITEM_EVENT)
                .data(mapper.writeValueAsString(updateUserItemMessageInput))
                .build();

            messageRepository.sendMessage(ITEM_QUEUE, mapper.writeValueAsString(commonMessageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeOfferUserItems(List<OfferUserItem> offerUserItems) {
        offerUserItemRepository.removeAll(offerUserItems);
    }

    private void deleteOffer(Offer offer) {
        offerRepository.delete(offer.getId());
    }
}
