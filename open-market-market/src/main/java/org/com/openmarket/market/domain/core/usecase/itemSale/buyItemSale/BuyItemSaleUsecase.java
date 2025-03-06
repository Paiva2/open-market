package org.com.openmarket.market.domain.core.usecase.itemSale.buyItemSale;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.*;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemSaleNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserBalanceException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.market.domain.core.usecase.itemSale.buyItemSale.exception.InvalidBuyException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;

// todo: add quantidade a comprar
@Service
@AllArgsConstructor
public class BuyItemSaleUsecase {
    private final static String WALLET_DATABASE_NAME = "open-market-wallet-db";
    private final static String BANK_ADM_EXTERNAL_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();

    private final UserRepository userRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final WalletRepository walletRepository;
    private final DatabaseLockRepository databaseLockRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final UserItemRepository userItemRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void execute(BuyItemSaleInput input, String externalUserId, UUID itemSaleId) {
        User user = findUser(externalUserId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (input.getQuantity() < 1) {
            input.setQuantity(1);
        }

        ItemSale itemSale = findItemOnSale(itemSaleId);

        if (itemSale.getQuantity() < input.getQuantity()) {
            throw new InvalidBuyException("Item on sale has no quantity available! Available: " + itemSale.getQuantity());
        }

        User userSelling = itemSale.getUserItem().getUser();

        checkExpirationDate(itemSale);
        checkItemSaleAcceptOnlyOffers(itemSale);
        checkUserSelling(user, userSelling);

        DatabaseLock databaseLock = null;

        try {
            databaseLock = lockUserWallet(user);

            UserWalletViewOutput userWallet = findWallet();
            checkBalanceAvailable(userWallet, itemSale);

            LinkedHashSet<CommonMessageDTO> messages = new LinkedHashSet<>();

            if (itemSale.getQuantity() == 1) {
                if (itemSale.getAcceptOffers()) {
                    removeItemOnSaleOffers(itemSale, messages);
                }
            }

            giveUserItemPurchased(itemSale, userSelling, user, input.getQuantity(), messages);
            sendSellerBuyValue(itemSale, user, userSelling, input.getQuantity(), messages);
            sendMessages(messages);
        } catch (Exception e) {
            throw e;
        } finally {
            removeUserLockWallet(databaseLock);
        }
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private ItemSale findItemOnSale(UUID itemSaleId) {
        return itemSaleRepository.findByIdWithDeps(itemSaleId).orElseThrow(ItemSaleNotFoundException::new);
    }

    private void checkItemSaleAcceptOnlyOffers(ItemSale itemSale) {
        if (itemSale.getOnlyOffers()) {
            throw new InvalidBuyException("Invalid buy. Item on sale accept only offers!");
        }
    }

    private void checkExpirationDate(ItemSale itemSale) {
        if (itemSale.getExpirationDate().before(new Date())) {
            throw new ItemSaleNotFoundException();
        }
    }

    private void checkUserSelling(User user, User userSelling) {
        if (user.getId().equals(userSelling.getId())) {
            throw new InvalidBuyException("Invalid buy. Can't buy your own item!");
        }
    }

    private DatabaseLock lockUserWallet(User user) {
        DatabaseLock databaseLock = DatabaseLock.builder()
            .databaseName(WALLET_DATABASE_NAME)
            .externalUserId(user.getExternalId())
            .build();

        return databaseLockRepository.saveLock(databaseLock);
    }

    private UserWalletViewOutput findWallet() {
        return walletRepository.getUserWalletView();
    }

    private void checkBalanceAvailable(UserWalletViewOutput userWallet, ItemSale itemSale) {
        if (userWallet.getBalance().compareTo(itemSale.getValue()) < 0) {
            throw new UserBalanceException("User has no balance to complete transaction!");
        }
    }

    private void removeItemOnSaleOffers(ItemSale itemSale, LinkedHashSet<CommonMessageDTO> messages) {
        List<Offer> offers = offerRepository.findAllByItemSaleNoPage(itemSale.getId());

        if (offers.isEmpty()) return;

        String bankAdminId = System.getenv(BANK_ADM_EXTERNAL_ID);

        List<UserItem> userItemsToSave = new ArrayList<>();
        List<OfferUserItem> offerUserItemsToRemove = new ArrayList<>();

        for (Offer offer : offers) {
            if (offer.getValue().compareTo(BigDecimal.ZERO) > 0) {
                String description = "Chargeback from offer made, offer refused id: " + offer.getId();

                messages.add(
                    giveWalletValue(bankAdminId,
                        offer.getUser().getExternalId(),
                        offer.getValue(),
                        description,
                        EnumTransactionType.TRANSFER
                    )
                );
            }

            if (!offer.getOfferUserItems().isEmpty()) {
                removeOfferUserItems(offer.getOfferUserItems(), offerUserItemsToRemove, userItemsToSave, messages);
            }
        }

        userItemRepository.persistAll(userItemsToSave);
        offerUserItemRepository.removeAll(offerUserItemsToRemove);
        offerRepository.removeOffers(offers);
    }

    private void removeOfferUserItems(List<OfferUserItem> offerUserItems, List<OfferUserItem> offerUserItemsToRemove, List<UserItem> userItemsToSave, LinkedHashSet<CommonMessageDTO> messages) {
        for (OfferUserItem offerUserItem : offerUserItems) {
            UserItem userItem = offerUserItem.getUserItem();
            User user = userItem.getUser();
            Item item = userItem.getItem();
            AttributeItem attributeItem = userItem.getAttribute();
            userItem.setQuantity(userItem.getQuantity() + offerUserItem.getQuantity());

            offerUserItemsToRemove.add(offerUserItem);
            userItemsToSave.add(userItem);

            UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItem.getQuantity());
            UpdateUserItemMessageInput updateInput = mountUpdateUserItemInput(user.getExternalId(), item.getExternalId(), attributeItem.getExternalId(), userItemInput);

            messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, updateInput));
        }
    }

    private void removeItemOnSale(ItemSale itemSale) {
        itemSaleRepository.remove(itemSale.getId());
    }

    private void persistItemSale(ItemSale itemSale) {
        itemSaleRepository.save(itemSale);
    }

    private void giveUserItemPurchased(ItemSale itemSale, User userSelling, User user, Integer quantityBuy, LinkedHashSet<CommonMessageDTO> messages) {
        UserItem userItemSold = itemSale.getUserItem();
        Item itemSold = userItemSold.getItem();
        AttributeItem attributeItemSold = userItemSold.getAttribute();

        UserItem userItemToSave = null;
        UserItem userItemToDelete = null;

        if (itemSold.getStackable()) {
            Optional<UserItem> userItemStackable = userItemRepository.findByUserAndItemExternalId(user.getId(), itemSold.getExternalId());

            if (userItemStackable.isPresent()) {
                userItemStackable.get().setQuantity(userItemStackable.get().getQuantity() + quantityBuy);

                userItemToSave = userItemStackable.get();

                UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItemStackable.get().getQuantity());
                UpdateUserItemMessageInput updateInput = mountUpdateUserItemInput(user.getExternalId(), userItemStackable.get().getItem().getExternalId(), userItemStackable.get().getAttribute().getExternalId(), userItemInput);

                messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, updateInput));
            } else {
                CreateUserItemMessageInput.UserItemAttributeInput attributeInput = new CreateUserItemMessageInput.UserItemAttributeInput(attributeItemSold.getAttributes());
                CreateUserItemMessageInput messageInput = mountCreateUserItemInput(user.getExternalId(), itemSold.getExternalId(), quantityBuy.longValue(), attributeInput);

                messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.CREATED, messageInput));
            }
        } else {
            userItemToSave = UserItem.builder()
                .id(UserItem.KeyId.builder()
                    .userId(user.getId())
                    .itemId(itemSold.getId())
                    .attributeId(attributeItemSold.getId())
                    .build()
                ).user(user)
                .item(itemSold)
                .attribute(attributeItemSold)
                .quantity(quantityBuy.longValue())
                .build();

            userItemToDelete = userItemSold;

            UpdateUserItemMessageInput.UserItemInput userItemInput = new UpdateUserItemMessageInput.UserItemInput(userItemToSave.getQuantity(), user.getExternalId());
            UpdateUserItemMessageInput updateInput = mountUpdateUserItemInput(userSelling.getExternalId(), itemSold.getExternalId(), attributeItemSold.getExternalId(), userItemInput);

            messages.add(mountCommonMessage(EnumMessageEvent.USER_ITEM_EVENT, EnumMessageType.UPDATED, updateInput));
        }

        itemSale.setQuantity(itemSale.getQuantity() - quantityBuy.longValue());

        if (itemSale.getQuantity() < 1) {
            removeItemOnSale(itemSale);
        } else {
            persistItemSale(itemSale);
        }

        if (userItemToDelete != null) {
            userItemRepository.remove(userItemToDelete);
        }

        if (userItemToSave != null) {
            userItemRepository.persist(userItemToSave);
        }
    }

    private void sendSellerBuyValue(ItemSale itemSale, User user, User userSelling, Integer buyQuantity, LinkedHashSet<CommonMessageDTO> messages) {
        String purchaseDescription = "Payment made to buy item on sale: " + itemSale.getUserItem().getItem().getName();
        CommonMessageDTO walletMessage = giveWalletValue(
            user.getExternalId(),
            userSelling.getExternalId(),
            itemSale.getValue().multiply(new BigDecimal(buyQuantity)),
            purchaseDescription,
            EnumTransactionType.PAYMENT
        );

        messages.add(walletMessage);
    }

    private void sendMessages(LinkedHashSet<CommonMessageDTO> messages) {
        try {
            for (CommonMessageDTO message : messages) {
                String queue = message.getEvent().equals(EnumMessageEvent.USER_ITEM_EVENT) ? ITEM_QUEUE : WALLET_QUEUE;

                messageRepository.sendMessage(queue, mapper.writeValueAsString(message));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    private UpdateUserItemMessageInput mountUpdateUserItemInput(String externalUserId, String externalItemId, String externalAttributeItemId, UpdateUserItemMessageInput.UserItemInput updateInput) {
        return UpdateUserItemMessageInput.builder()
            .externalUserId(externalUserId)
            .externalItemId(externalItemId)
            .externalAttributeId(externalAttributeItemId)
            .userItemInput(updateInput)
            .build();
    }

    private CreateUserItemMessageInput mountCreateUserItemInput(String externalUserId, String externalItemId, Long quantity, CreateUserItemMessageInput.UserItemAttributeInput attributeInput) {
        return CreateUserItemMessageInput.builder()
            .externalUserId(externalUserId)
            .externalItemId(externalItemId)
            .userItemInput(new CreateUserItemMessageInput.UserItemInput(quantity))
            .itemAttribute(attributeInput)
            .build();
    }

    public CommonMessageDTO giveWalletValue(String userGivingId, String userReceivingId, BigDecimal value, String description, EnumTransactionType transactionType) {
        try {
            WalletMessageInput input = WalletMessageInput.builder()
                .externalUserId(userGivingId)
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .externalUserTargetId(userReceivingId)
                    .description(description)
                    .value(value)
                    .type(transactionType.toString())
                    .build()
                ).build();

            return CommonMessageDTO.builder()
                .event(EnumMessageEvent.WALLET_EVENT)
                .type(EnumMessageType.NEW_TRANSACTION)
                .data(mapper.writeValueAsString(input))
                .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void removeUserLockWallet(DatabaseLock databaseLock) {
        if (databaseLock == null) return;

        databaseLockRepository.removeLock(databaseLock);
    }
}
