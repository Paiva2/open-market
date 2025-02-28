package org.com.openmarket.market.domain.core.usecase.offer.acceptOffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.CreateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemSaleNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.OfferNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception.InvalidOfferException;
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
public class AcceptOfferUsecase {
    private final static String BANK_ADM_EXTERNAL_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void execute(String externalUserId, UUID itemSaleId, UUID offerId) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        ItemSale itemSale = findItemSale(itemSaleId);

        checkItemSaleConstraints(itemSale);
        checkUserItemSale(user, itemSale);

        List<Offer> allOffers = findAllOffers(itemSaleId);
        Offer offerAccepted = findOfferAccepted(allOffers, offerId);
        List<Offer> allOffersWithoutAccepted = getAllOffersWithoutAccepted(allOffers, offerAccepted.getId());

        User userMadeOffer = offerAccepted.getUser();

        List<OfferUserItem> allOfferUserItemsItemSale = findAllItemsOfferedInItemSale(itemSale.getId());
        List<OfferUserItem> offerUserItemsNonAcceptedOffer = findAllUserItemsFromNonAcceptedOffer(allOfferUserItemsItemSale, offerAccepted.getId());
        List<OfferUserItem> offerUserItemsAcceptedOffer = findAllOfferUserItemsAcceptedOffer(allOfferUserItemsItemSale, offerAccepted.getId());

        LinkedHashSet<CommonMessageDTO> messages = new LinkedHashSet<>();

        if (!offerUserItemsAcceptedOffer.isEmpty()) {
            giveUserOfferItems(offerUserItemsAcceptedOffer, user, messages);
        }

        if (offerAccepted.getValue().compareTo(BigDecimal.ZERO) > 0) {
            String description = "Transaction value from offer accepted: " + offerAccepted.getId();
            giveWalletValue(offerAccepted, user, description, messages);
        }

        removeOfferAccepted(offerAccepted);
        removeItemSale(itemSale, allOffersWithoutAccepted, offerUserItemsNonAcceptedOffer, messages);
        giveItemSale(itemSale, userMadeOffer, user, messages);
        sendMessages(messages);
    }

    private List<Offer> findAllOffers(UUID itemSaleId) {
        return offerRepository.findAllByItemSaleNoPage(itemSaleId);
    }

    private Offer findOfferAccepted(List<Offer> offers, UUID offerId) {
        return offers.stream().filter(offer -> offer.getId().equals(offerId)).findFirst().orElseThrow(OfferNotFoundException::new);
    }

    private List<Offer> getAllOffersWithoutAccepted(List<Offer> offers, UUID offerAcceptedId) {
        return offers.stream().filter(offer -> !offer.getId().equals(offerAcceptedId)).toList();
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserItemSale(User user, ItemSale itemSale) {
        if (!itemSale.getUserItem().getUser().getId().equals(user.getId())) {
            throw new InvalidOfferException("Item on sale does not belong to user!");
        }
    }

    private ItemSale findItemSale(UUID itemSaleId) {
        return itemSaleRepository.findByIdWithDeps(itemSaleId).orElseThrow(ItemSaleNotFoundException::new);
    }

    private void checkItemSaleConstraints(ItemSale itemSale) {
        if (itemSale.getExpirationDate().before(new Date())) {
            throw new ItemSaleNotFoundException();
        }

        if (!itemSale.getAcceptOffers()) {
            throw new InvalidOfferException("Item sale does not accept offers!");
        }
    }

    private List<OfferUserItem> findAllItemsOfferedInItemSale(UUID itemSaleId) {
        return offerUserItemRepository.findAllByItemSale(itemSaleId);
    }

    private List<OfferUserItem> findAllUserItemsFromNonAcceptedOffer(List<OfferUserItem> offerUserItems, UUID offerId) {
        return offerUserItems.stream().filter(ofu -> !ofu.getOffer().getId().equals(offerId)).toList();
    }

    private List<OfferUserItem> findAllOfferUserItemsAcceptedOffer(List<OfferUserItem> offerUserItems, UUID offerId) {
        return offerUserItems.stream().filter(ofu -> ofu.getOffer().getId().equals(offerId)).toList();
    }

    private void giveUserOfferItems(List<OfferUserItem> offerUserItems, User user, LinkedHashSet<CommonMessageDTO> messages) {
        List<UserItem> userItemsToSave = new ArrayList<>();
        List<UserItem> userItemsToRemove = new ArrayList<>();

        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItemOffered = offerUserItem.getUserItem();
            Item item = userItemOffered.getItem();
            AttributeItem attributeItem = userItemOffered.getAttribute();

            Long quantityOffered = offerUserItem.getQuantity();

            if (item.getStackable()) {
                Optional<UserItem> checkUserHasUserItem = findUserItem(user, item);

                if (checkUserHasUserItem.isPresent()) {
                    UserItem userItemFound = checkUserHasUserItem.get();
                    userItemFound.setQuantity(userItemFound.getQuantity() + quantityOffered);

                    userItemsToSave.add(userItemFound);

                    UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItemFound.getQuantity());
                    UpdateUserItemMessageInput messageInput = mountUpdateUserItemInput(user.getExternalId(), userItemFound.getItem().getExternalId(), userItemFound.getAttribute().getExternalId(), userItemInput);

                    messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, messageInput));
                } else {
                    CreateUserItemMessageInput.UserItemAttributeInput attributeInput = new CreateUserItemMessageInput.UserItemAttributeInput(attributeItem.getAttributes());
                    CreateUserItemMessageInput messageInput = mountCreateUserItemInput(user.getExternalId(), item.getExternalId(), quantityOffered, attributeInput);

                    messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.CREATED, messageInput));
                }
            } else {
                UserItem newUserItem = UserItem.builder()
                    .id(new UserItem.KeyId(attributeItem.getId(), user.getId(), item.getId()))
                    .user(user)
                    .item(item)
                    .attribute(attributeItem)
                    .quantity(quantityOffered)
                    .build();

                userItemsToSave.add(newUserItem);
                userItemsToRemove.add(userItemOffered);

                String userMakeOfferExtId = userItemOffered.getUser().getExternalId();

                UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(quantityOffered, user.getExternalId());
                UpdateUserItemMessageInput messageInput = mountUpdateUserItemInput(userMakeOfferExtId, item.getExternalId(), attributeItem.getExternalId(), userItemInput);

                messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, messageInput));
            }
        }

        removeAllItemsOffered(offerUserItems);

        if (!userItemsToRemove.isEmpty()) {
            removeAllUserItem(userItemsToRemove);
        }

        saveAllUserItem(userItemsToSave);
    }

    private void removeAllUserItem(List<UserItem> userItems) {
        userItemRepository.removeAll(userItems);
    }

    private void saveAllUserItem(List<UserItem> userItems) {
        userItemRepository.persistAll(userItems);
    }

    private Optional<UserItem> findUserItem(User user, Item item) {
        return userItemRepository.findByUserAndItemExternalId(user.getId(), item.getExternalId());
    }

    private UpdateUserItemMessageInput mountUpdateUserItemInput(String extUserId, String extItemId, String extAttributeId, UpdateUserItemMessageInput.UserItemInput userItemInput) {
        return UpdateUserItemMessageInput.builder()
            .externalUserId(extUserId)
            .externalItemId(extItemId)
            .externalAttributeId(extAttributeId)
            .userItemInput(userItemInput)
            .build();
    }

    private CreateUserItemMessageInput mountCreateUserItemInput(String extUserId, String extItemId, Long quantity, CreateUserItemMessageInput.UserItemAttributeInput attributeItem) {
        return CreateUserItemMessageInput.builder()
            .externalUserId(extUserId)
            .externalItemId(extItemId)
            .userItemInput(new CreateUserItemMessageInput.UserItemInput(quantity))
            .itemAttribute(attributeItem)
            .build();
    }

    private CommonMessageDTO mountCommonMessage(EnumMessageEvent event, EnumMessageType type, Object data) {
        try {
            return CommonMessageDTO.builder()
                .event(event)
                .type(type)
                .data(mapper.writeValueAsString(data))
                .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void giveWalletValue(Offer offer, User user, String description, LinkedHashSet<CommonMessageDTO> messages) {
        WalletMessageInput walletMessageInput = mountWalletMessage(user, offer, description);
        CommonMessageDTO commonMessage = mountCommonMessage(EnumMessageEvent.WALLET_EVENT, EnumMessageType.NEW_TRANSACTION, walletMessageInput);

        messages.add(commonMessage);
    }

    private WalletMessageInput mountWalletMessage(User user, Offer offer, String description) {
        return WalletMessageInput.builder()
            .externalUserId(System.getenv(BANK_ADM_EXTERNAL_ID))
            .transaction(WalletMessageInput.NewTransaction.builder()
                .externalUserTargetId(user.getExternalId())
                .description(description)
                .value(offer.getValue())
                .type(EnumTransactionType.TRANSFER.name())
                .build()
            ).build();
    }

    private void giveItemSale(ItemSale itemSale, User userMadeOffer, User user, LinkedHashSet<CommonMessageDTO> messages) {
        UserItem userItemOnSale = itemSale.getUserItem();

        Item item = userItemOnSale.getItem();
        AttributeItem attributeItem = userItemOnSale.getAttribute();

        List<UserItem> userItemsToSave = new ArrayList<>();
        List<UserItem> userItemsToRemove = new ArrayList<>();

        if (item.getStackable()) {
            Optional<UserItem> checkUserHasUserItem = findUserItem(userMadeOffer, item);

            if (checkUserHasUserItem.isPresent()) {
                UserItem userItemFound = checkUserHasUserItem.get();
                userItemFound.setQuantity(userItemFound.getQuantity() + itemSale.getQuantity());

                userItemsToSave.add(userItemFound);

                UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItemFound.getQuantity());
                UpdateUserItemMessageInput messageInput = mountUpdateUserItemInput(userMadeOffer.getExternalId(), item.getExternalId(), attributeItem.getExternalId(), userItemInput);
                messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, messageInput));
            } else {
                CreateUserItemMessageInput.UserItemAttributeInput attributeInput = new CreateUserItemMessageInput.UserItemAttributeInput(attributeItem.getAttributes());
                CreateUserItemMessageInput messageInput = mountCreateUserItemInput(userMadeOffer.getExternalId(), item.getExternalId(), itemSale.getQuantity(), attributeInput);

                messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.CREATED, messageInput));
            }
        } else {
            UserItem newUserItem = UserItem.builder()
                .id(new UserItem.KeyId(attributeItem.getId(), userMadeOffer.getId(), item.getId()))
                .user(userMadeOffer)
                .item(item)
                .attribute(attributeItem)
                .quantity(itemSale.getQuantity())
                .build();

            userItemsToSave.add(newUserItem);
            userItemsToRemove.add(userItemOnSale);

            UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(itemSale.getQuantity(), userMadeOffer.getExternalId());
            UpdateUserItemMessageInput messageInput = mountUpdateUserItemInput(user.getExternalId(), item.getExternalId(), attributeItem.getExternalId(), userItemInput);
            messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, messageInput));
        }

        removeAllUserItem(userItemsToRemove);
        saveAllUserItem(userItemsToSave);
    }

    private void removeOfferAccepted(Offer offer) {
        offerRepository.delete(offer.getId());
    }

    private void removeItemSale(ItemSale itemSale, List<Offer> allOffersMade, List<OfferUserItem> allItemsOffered, LinkedHashSet<CommonMessageDTO> messages) {
        List<UserItem> userItemsToSave = new ArrayList<>();

        removeAllOffersNonAccepted(allOffersMade, messages);

        if (!allItemsOffered.isEmpty()) {
            giveBackAllItemsOfferedNonAccepted(allItemsOffered, userItemsToSave, messages);
        }

        saveAllUserItem(userItemsToSave);
        removeAllItemsOffered(allItemsOffered);
        removeAllOffers(allOffersMade);
        removeItemOnSale(itemSale);
    }

    private void removeAllOffersNonAccepted(List<Offer> offers, LinkedHashSet<CommonMessageDTO> messages) {
        for (Offer offer : offers) {
            User userMadeOffer = offer.getUser();

            if (offer.getValue().compareTo(BigDecimal.ZERO) > 0) {
                String description = "Chargeback from offer made, offer refused id: " + offer.getId();
                giveWalletValue(offer, userMadeOffer, description, messages);
            }
        }
    }

    private void giveBackAllItemsOfferedNonAccepted(List<OfferUserItem> offerUserItems, List<UserItem> userItemsToSave, LinkedHashSet<CommonMessageDTO> messages) {
        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItem = offerUserItem.getUserItem();
            User user = userItem.getUser();
            Item item = userItem.getItem();
            AttributeItem attributeItem = userItem.getAttribute();
            Long quantityOffered = offerUserItem.getQuantity();

            userItem.setQuantity(userItem.getQuantity() + quantityOffered);

            userItemsToSave.add(userItem);

            UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItem.getQuantity());
            UpdateUserItemMessageInput messageInput = mountUpdateUserItemInput(user.getExternalId(), item.getExternalId(), attributeItem.getExternalId(), userItemInput);
            messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, messageInput));
        }
    }

    private void removeAllItemsOffered(List<OfferUserItem> offerUserItems) {
        offerUserItemRepository.removeAll(offerUserItems);
    }

    private void removeAllOffers(List<Offer> offers) {
        offerRepository.removeOffers(offers);
    }

    private void removeItemOnSale(ItemSale itemSale) {
        itemSaleRepository.remove(itemSale.getId());
    }

    private void sendMessages(LinkedHashSet<CommonMessageDTO> messages) {
        for (CommonMessageDTO messageDTO : messages) {
            try {
                String queue = messageDTO.getEvent().equals(EnumMessageEvent.WALLET_EVENT) ? WALLET_QUEUE : ITEM_QUEUE;

                messageRepository.sendMessage(queue, mapper.writeValueAsString(messageDTO));
            } catch (Exception e) {
                log.info("Error while sending message... {}", e.getMessage());
            }
        }
    }
}
