package org.com.openmarket.market.domain.core.usecase.offer.editOffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.OfferNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserBalanceException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserItemNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;
import org.com.openmarket.market.domain.core.usecase.offer.editOffer.dto.EditOfferInput;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception.InvalidOfferException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;
import static org.com.openmarket.market.domain.enumeration.EnumMessageEvent.WALLET_EVENT;

@Service
@AllArgsConstructor
public class EditOfferUsecase {
    private final static String WALLET_DATABASE_NAME = "open-market-wallet-db";
    private final static String BANK_ADM_EXTERNAL_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;
    private final WalletRepository walletRepository;
    private final DatabaseLockRepository databaseLockRepository;

    @Transactional
    public void execute(String externalUserId, UUID offerId, EditOfferInput input, String authToken) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        Offer offer = findOffer(offerId);
        checkUserPermission(user, offer);

        checkItemSaleExpiration(offer.getItemSale());

        LinkedList<CommonMessageDTO> commonMessageDTOS = new LinkedList<>();

        handleOfferUserItems(user, offer, input, commonMessageDTOS);

        checkUserWalletLock(user);
        DatabaseLock walletLock = lockWallet(user);

        handleOfferValue(user, offer, offer.getItemSale(), input, commonMessageDTOS, authToken, walletLock);

        sendAllMessages(commonMessageDTOS, walletLock);
        unlockWallet(walletLock);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserItemNotFoundException::new);
    }

    private Offer findOffer(UUID offerId) {
        return offerRepository.findById(offerId).orElseThrow(OfferNotFoundException::new);
    }

    private void checkUserPermission(User user, Offer offer) {
        User offerUser = offer.getUser();

        if (!user.getId().equals(offerUser.getId())) {
            throw new ForbiddenException("Offer does not belong to user!");
        }
    }

    private void checkItemSaleExpiration(ItemSale itemSale) {
        if (itemSale.getExpirationDate().before(new Date())) {
            throw new OfferNotFoundException();
        }
    }

    private void handleOfferUserItems(User user, Offer offer, EditOfferInput input, LinkedList<CommonMessageDTO> commonMessageDTOS) {
        List<OfferUserItem> currentOfferUserItems = offerUserItemRepository.findByOfferIdWithDeps(offer.getId());

        if (input.getUserItems().isEmpty() && !currentOfferUserItems.isEmpty()) {
            removeAllOfferUserItems(currentOfferUserItems, commonMessageDTOS);
            return;
        }

        List<String> currExternalItemAttributeIds = currentOfferUserItems.stream().map(OfferUserItem::getUserItem).map(UserItem::getAttribute).map(AttributeItem::getExternalId).toList();

        boolean hasDifferentOfferedItems = input.getUserItems().stream().map(EditOfferInput.UserItemInput::getExternalAttributeId).anyMatch(externalAttributeId -> !currExternalItemAttributeIds.contains(externalAttributeId));
        boolean hasAddedOrRemovedOfferedItems = input.getUserItems().size() != currExternalItemAttributeIds.size();

        boolean willTriggerChange = hasDifferentOfferedItems || hasAddedOrRemovedOfferedItems;

        if (!willTriggerChange) {
            willTriggerChange = checkAnyQuantityChanged(currentOfferUserItems, input);
        }

        if (willTriggerChange) {
            removeAllOfferUserItems(currentOfferUserItems, commonMessageDTOS);

            List<OfferUserItem> offerUserItems = fillOfferUserItems(user, offer, input, commonMessageDTOS);
            offerUserItemRepository.persistAll(offerUserItems);
        }
    }

    private List<OfferUserItem> fillOfferUserItems(User user, Offer offer, EditOfferInput input, LinkedList<CommonMessageDTO> commonMessageDTOS) {
        List<OfferUserItem> offerUserItems = new ArrayList<>();
        List<String> userItemsNotValid = new ArrayList<>();
        List<UserItem> userItemsToUpdate = new ArrayList<>();

        for (EditOfferInput.UserItemInput userItemInput : input.getUserItems()) {
            Optional<UserItem> userItem = userItemRepository.findUserItemWithQuantity(user.getId(), userItemInput.getExternalItemId(), userItemInput.getExternalAttributeId());

            if (userItem.isEmpty() || (userItem.get().getQuantity() < userItemInput.getQuantity() || userItem.get().getItem().getUnique())) {
                userItemsNotValid.add(userItemInput.getExternalItemId());
            } else {
                UserItem userItemFound = userItem.get();
                Optional<OfferUserItem> userAlreadyOfferedUserItemInOther = offerUserItemRepository.findByUserItemAndAttribute(user.getId(), userItemFound.getItem().getId(), userItemFound.getAttribute().getId());

                if (userAlreadyOfferedUserItemInOther.isPresent() && !userItemFound.getItem().getStackable()) {
                    userItemsNotValid.add(userItemInput.getExternalItemId());
                } else if (userItemInput.getQuantity() > 0L) {
                    OfferUserItem offerUserItem = OfferUserItem.builder()
                        .userItem(userItemFound)
                        .offer(offer)
                        .quantity(userItemInput.getQuantity())
                        .build();

                    userItemFound.setQuantity(userItemFound.getQuantity() - userItemInput.getQuantity());

                    offerUserItems.add(offerUserItem);
                    userItemsToUpdate.add(userItemFound);
                }
            }
        }

        if (!userItemsNotValid.isEmpty()) {
            String message = "User items not found or not available! {0}";
            throw new UserItemNotFoundException(MessageFormat.format(message, userItemsNotValid.toString()));
        }

        if (!userItemsToUpdate.isEmpty()) {
            for (UserItem userItem : userItemsToUpdate) {
                Item item = userItem.getItem();
                AttributeItem attributeItem = userItem.getAttribute();

                CommonMessageDTO commonMessageDTO = mountUserItemUpdateMessage(item.getExternalId(), attributeItem.getExternalId(), user.getExternalId(), userItem.getQuantity());
                commonMessageDTOS.add(commonMessageDTO);
            }

            userItemRepository.persistAll(userItemsToUpdate);
        }

        return offerUserItems;
    }

    private boolean checkAnyQuantityChanged(List<OfferUserItem> offerUserItems, EditOfferInput input) {
        boolean hasChangedAnyQuantity = false;

        for (OfferUserItem offerUserItem : offerUserItems) {
            Optional<EditOfferInput.UserItemInput> offerUserItemInput = input.getUserItems().stream().filter(inputUserItems -> inputUserItems.getExternalAttributeId().equals(offerUserItem.getUserItem().getAttribute().getExternalId())).findFirst();

            if (offerUserItemInput.isPresent()) {
                Long quantityOnOfferInput = offerUserItemInput.get().getQuantity();

                if (!quantityOnOfferInput.equals(offerUserItem.getQuantity())) {
                    hasChangedAnyQuantity = true;
                    break;
                }
            }
        }

        return hasChangedAnyQuantity;
    }

    private void removeAllOfferUserItems(List<OfferUserItem> offerUserItems, LinkedList<CommonMessageDTO> commonMessageDTOS) {
        List<UserItem> userItemsUpdated = new ArrayList<>();

        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItem = offerUserItem.getUserItem();
            User user = userItem.getUser();
            Item item = userItem.getItem();
            AttributeItem attributeItem = userItem.getAttribute();
            Long quantityOnOffer = offerUserItem.getQuantity();

            userItem.setQuantity(userItem.getQuantity() + quantityOnOffer);

            userItemsUpdated.add(userItem);

            CommonMessageDTO commonMessageDTO = mountUserItemUpdateMessage(item.getExternalId(), attributeItem.getExternalId(), user.getExternalId(), userItem.getQuantity());
            commonMessageDTOS.add(commonMessageDTO);
        }

        userItemRepository.persistAll(userItemsUpdated);
        offerUserItemRepository.removeAll(offerUserItems);
    }

    private CommonMessageDTO mountUserItemUpdateMessage(String externalItemId, String externalAttributeId, String externalUserId, Long quantity) {
        try {
            UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
                .externalUserId(externalUserId)
                .externalItemId(externalItemId)
                .externalAttributeId(externalAttributeId)
                .userItemInput(new UpdateUserItemMessageInput.UserItemInput(quantity))
                .build();

            return CommonMessageDTO.builder()
                .type(EnumMessageType.UPDATED)
                .event(EnumMessageEvent.USER_ITEM_EVENT)
                .data(mapper.writeValueAsString(updateUserItemMessageInput))
                .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private CommonMessageDTO mountChangeWalletValueMessage(User user, ItemSale itemSale, BigDecimal value, boolean newValueIsHigher, String authToken, DatabaseLock walletLock) {
        String bankAdminId = System.getenv(BANK_ADM_EXTERNAL_ID);

        try {

            WalletMessageInput messageInput;
            String description = MessageFormat.format("Offer made to Item on sale identifier: {0}", itemSale.getId());

            if (newValueIsHigher) {
                UserWalletViewOutput userWalletViewOutput = walletRepository.getUserWalletView(authToken);

                if (userWalletViewOutput.getBalance().compareTo(value) < 0) {
                    throw new UserBalanceException("User has no balance available!");
                }

                messageInput = mountWalletMessageInput(user.getExternalId(), bankAdminId, description, value);
            } else {
                messageInput = mountWalletMessageInput(bankAdminId, user.getExternalId(), description, value);
            }

            return CommonMessageDTO.builder()
                .type(EnumMessageType.NEW_TRANSACTION)
                .event(WALLET_EVENT)
                .data(mapper.writeValueAsString(messageInput))
                .build();
        } catch (UserBalanceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            unlockWallet(walletLock);
        }
    }

    private WalletMessageInput mountWalletMessageInput(String userIdPaying, String userIdReceiving, String description, BigDecimal value) {
        return WalletMessageInput.builder()
            .externalUserId(userIdPaying)
            .transaction(WalletMessageInput.NewTransaction.builder()
                .externalUserTargetId(userIdReceiving)
                .description(description)
                .value(value)
                .type(EnumTransactionType.PAYMENT.name())
                .build()
            ).build();
    }

    private void sendAllMessages(LinkedList<CommonMessageDTO> commonMessageDTOS, DatabaseLock walletLock) {
        for (CommonMessageDTO commonMessageDTO : commonMessageDTOS) {
            try {
                String queue = commonMessageDTO.getEvent().equals(WALLET_EVENT) ? WALLET_QUEUE : ITEM_QUEUE;

                messageRepository.sendMessage(queue, mapper.writeValueAsString(commonMessageDTO));
            } catch (Exception e) {
                unlockWallet(walletLock);
                throw new RuntimeException(e);
            }
        }
    }

    private void handleOfferValue(User user, Offer offer, ItemSale itemSale, EditOfferInput input, LinkedList<CommonMessageDTO> commonMessageDTOS, String authToken, DatabaseLock walletLock) {
        if (offer.getValue().compareTo(input.getValue()) == 0) return;

        if (offer.getValue().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidOfferException("Value can't be less than 0!");
        }

        BigDecimal differenceVal;
        boolean newValueIsHigher = false;

        if (offer.getValue().compareTo(input.getValue()) > 0) {
            differenceVal = offer.getValue().subtract(input.getValue());
        } else {
            differenceVal = input.getValue().subtract(offer.getValue());
            newValueIsHigher = true;
        }

        offer.setValue(input.getValue());
        offerRepository.persist(offer);

        CommonMessageDTO commonMessageDTO = mountChangeWalletValueMessage(user, itemSale, differenceVal, newValueIsHigher, authToken, walletLock);
        commonMessageDTOS.add(commonMessageDTO);
    }

    private void checkUserWalletLock(User user) {
        Optional<DatabaseLock> databaseLock = databaseLockRepository.getLockByDatabaseAndUser(WALLET_DATABASE_NAME, user.getExternalId());

        if (databaseLock.isPresent()) {
            throw new ConflictException("Another operation is being made on user wallet. Try again later!");
        }
    }

    private DatabaseLock lockWallet(User user) {
        DatabaseLock databaseLock = DatabaseLock.builder()
            .externalUserId(user.getExternalId())
            .databaseName(WALLET_DATABASE_NAME)
            .build();

        return databaseLockRepository.saveLock(databaseLock);
    }

    private void unlockWallet(DatabaseLock databaseLock) {
        databaseLockRepository.removeLock(databaseLock);
    }
}
