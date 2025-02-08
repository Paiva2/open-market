package org.com.openmarket.market.domain.core.usecase.offer.makeOffer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.*;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.*;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ConflictException;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.dto.MakeOfferInput;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.dto.OfferUserItemAlreadyOfferedException;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception.InvalidItemSaleQuantityException;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception.InvalidOfferException;
import org.com.openmarket.market.domain.core.usecase.offer.makeOffer.exception.OfferAlreadyMadeException;
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

@Service
@AllArgsConstructor
public class MakeOfferUsecase {
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static String WALLET_DATABASE_NAME = "open-market-wallet-db";
    private final static String BANK_ADM_WALLET_ID = "BANK_ADM_WALLET_ID";

    private final UserRepository userRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final UserItemRepository userItemRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;
    private final WalletRepository walletRepository;
    private final DatabaseLockRepository databaseLockRepository;
    private final MessageRepository messageRepository;

    @Transactional
    public void execute(UUID itemSaleId, MakeOfferInput input, String externalId, String authToken) {
        User user = findUser(externalId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        ItemSale itemSale = findItemSale(itemSaleId);

        checkUserAlreadyOffered(user, itemSale);

        checkItemSaleOwner(itemSale, user);
        checkItemSaleExpDate(itemSale);
        checkOfferConstraints(itemSale, input);
        checkItemSaleQuantity(itemSale);

        checkUserWalletLock(user);
        DatabaseLock lock = lockUserWallet(user);

        try {
            UserWalletViewOutput userWallet = findUserWallet(authToken);

            Offer offer = fillOffer(user, itemSale, input);
            offer = persistOffer(offer);

            boolean hasUserItemsOffered = input.getUserItems() != null && !input.getUserItems().isEmpty();

            List<UserItem> userItemsUpdated = null;

            if (hasUserItemsOffered) {
                HashMap<String, UserItem> userItems = findUserItems(user, input);
                checkUserItemAlreadyOffered(user, userItems);

                List<OfferUserItem> offerUserItems = fillOfferUserItems(offer, userItems, input);
                userItemsUpdated = decreaseUserItemsQuantityOffered(userItems, input);

                persistOfferUserItems(offerUserItems);
            }

            if (input.getValue() != null && input.getValue().compareTo(BigDecimal.ZERO) > 0) {
                checkUserWalletValue(userWallet, input);
                decreaseWalletValue(user, itemSale, input.getValue());
            }

            if (userItemsUpdated != null) {
                sendUserItemQuantityDecreaseMessage(user, userItemsUpdated);
            }

            unlockUserWallet(lock);
        } catch (Exception e) {
            unlockUserWallet(lock);
            throw e;
        }
    }

    private User findUser(String externalId) {
        return userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);
    }

    private void checkUserAlreadyOffered(User user, ItemSale itemSale) {
        Optional<Offer> offer = offerRepository.findByItemSaleAndUser(itemSale.getId(), user.getId());

        if (offer.isPresent()) {
            throw new OfferAlreadyMadeException("User already has made an offer.");
        }
    }

    private ItemSale findItemSale(UUID itemSaleId) {
        return itemSaleRepository.findByIdWithDeps(itemSaleId).orElseThrow(ItemSaleNotFoundException::new);
    }

    private Offer fillOffer(User user, ItemSale itemSale, MakeOfferInput input) {
        return Offer.builder()
            .value(input.getValue())
            .user(user)
            .itemSale(itemSale)
            .build();
    }

    private Offer persistOffer(Offer offer) {
        return offerRepository.persist(offer);
    }

    private void checkItemSaleQuantity(ItemSale itemSale) {
        if (itemSale.getQuantity() < 1) {
            throw new InvalidItemSaleQuantityException("Invalid item sale quantity. No quantities found!");
        }
    }

    private void checkUserWalletLock(User user) {
        Optional<DatabaseLock> databaseLock = databaseLockRepository.getLockByDatabaseAndUser(WALLET_DATABASE_NAME, user.getExternalId());

        if (databaseLock.isPresent()) {
            throw new ConflictException("Another operation is being made on user wallet. Try again later!");
        }
    }

    private DatabaseLock lockUserWallet(User user) {
        DatabaseLock databaseLock = new DatabaseLock(WALLET_DATABASE_NAME, user.getExternalId());
        return databaseLockRepository.saveLock(databaseLock);
    }

    private UserWalletViewOutput findUserWallet(String authToken) {
        return walletRepository.getUserWalletView(authToken);
    }

    private void checkUserWalletValue(UserWalletViewOutput userWallet, MakeOfferInput input) {
        if (userWallet.getBalance().compareTo(input.getValue()) < 0) {
            throw new UserBalanceException("User has no balance available.");
        }
    }

    private void decreaseWalletValue(User user, ItemSale itemSale, BigDecimal value) {
        String bankAdminWalletId = System.getenv(BANK_ADM_WALLET_ID);

        if (bankAdminWalletId == null) {
            throw new RuntimeException("Bank admin wallet id is null!");
        }

        try {
            WalletMessageInput walletMessageInput = WalletMessageInput.builder()
                .externalUserId(user.getExternalId())
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .targetWalletId(UUID.fromString(bankAdminWalletId))
                    .description(MessageFormat.format("Offer made to Item on sale identifier: {0}", itemSale.getId()))
                    .value(value)
                    .type(EnumTransactionType.TRANSFER.name())
                    .build()
                ).build();

            CommonMessageDTO commonMessage = CommonMessageDTO.builder()
                .type(EnumMessageType.NEW_TRANSACTION)
                .event(EnumMessageEvent.WALLET_EVENT)
                .data(mapper.writeValueAsString(walletMessageInput))
                .build();

            messageRepository.sendMessage(WALLET_QUEUE, mapper.writeValueAsString(commonMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void sendUserItemQuantityDecreaseMessage(User user, List<UserItem> userItems) {
        try {
            for (UserItem userItem : userItems) {
                UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
                    .externalUserId(user.getExternalId())
                    .externalItemId(userItem.getItem().getExternalId())
                    .externalAttributeId(userItem.getAttribute().getExternalId())
                    .userItemInput(new UpdateUserItemMessageInput.UserItemInput(userItem.getQuantity()))
                    .build();

                CommonMessageDTO commonMessage = CommonMessageDTO.builder()
                    .type(EnumMessageType.UPDATED)
                    .event(EnumMessageEvent.USER_ITEM_EVENT)
                    .data(mapper.writeValueAsString(updateUserItemMessageInput))
                    .build();

                messageRepository.sendMessage(ITEM_QUEUE, mapper.writeValueAsString(commonMessage));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkItemSaleOwner(ItemSale itemSale, User user) {
        if (itemSale.getUserItem().getUser().getId().equals(user.getId())) {
            throw new InvalidOfferException("Invalid offer. Can't make an offer on your own item on sale!");
        }
    }

    private void checkItemSaleExpDate(ItemSale itemSale) {
        Date now = new Date();

        if (now.after(itemSale.getExpirationDate())) {
            throw new ItemSaleNotFoundException();
        }
    }

    private void checkOfferConstraints(ItemSale itemSale, MakeOfferInput input) {
        if (!itemSale.getAcceptOffers()) {
            throw new InvalidOfferException("This sale doesn't allow offers!");
        }

        if ((input.getUserItems() == null || input.getUserItems().isEmpty()) && input.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidOfferException("Invalid offer. Offers must have an value or items!");
        }
    }

    private HashMap<String, UserItem> findUserItems(User user, MakeOfferInput input) {
        HashMap<String, UserItem> userItems = new HashMap<>();
        List<String> userItemsNotAvailable = new ArrayList<>();
        List<String> userItemsUnique = new ArrayList<>();

        for (MakeOfferInput.UserItemInput userItemInput : input.getUserItems()) {
            Optional<UserItem> userItem = userItemRepository.findUserItemWithQuantity(user.getId(), userItemInput.getExternalItemId(), userItemInput.getExternalAttributeId());

            if (userItem.isEmpty() || userItem.get().getQuantity() < userItemInput.getQuantity() || !userItem.get().getItem().getActive()) {
                userItemsNotAvailable.add(userItemInput.getExternalItemId());
            } else if (userItem.get().getItem().getUnique()) {
                userItemsUnique.add(userItemInput.getExternalItemId());
            } else {
                UserItem userItemFound = userItem.get();
                userItems.put(userItemFound.getAttribute().getExternalId(), userItemFound);
            }
        }

        if (!userItemsNotAvailable.isEmpty()) {
            String message = "User items not found or without quantity available! {0}";
            throw new UserItemNotFoundException(MessageFormat.format(message, userItemsNotAvailable.toString()));
        }

        if (!userItemsUnique.isEmpty()) {
            String message = "Cant offer unique items! {0}";
            throw new UserItemNotFoundException(MessageFormat.format(message, userItemsUnique.toString()));
        }

        return userItems;
    }

    private void checkUserItemAlreadyOffered(User user, HashMap<String, UserItem> userItemsMap) {
        List<UserItem> userItemsNonStackable = userItemsMap.values().stream().filter(userItem -> !userItem.getItem().getStackable()).toList();
        List<String> alreadyOfferedItems = new ArrayList<>();

        for (UserItem userItem : userItemsNonStackable) {
            Optional<OfferUserItem> offerUserItem = offerUserItemRepository.findByUserItemAndAttribute(user.getId(), userItem.getItem().getId(), userItem.getAttribute().getId());

            if (offerUserItem.isPresent()) {
                alreadyOfferedItems.add(offerUserItem.get().getId().toString());
            }
        }

        if (!alreadyOfferedItems.isEmpty()) {
            throw new OfferUserItemAlreadyOfferedException("Can't offer user items already offered if they are not stackable! User Items: " + alreadyOfferedItems);
        }
    }

    private List<OfferUserItem> fillOfferUserItems(Offer offer, HashMap<String, UserItem> userItems, MakeOfferInput input) {
        List<OfferUserItem> offerUserItems = new ArrayList<>();

        for (MakeOfferInput.UserItemInput userItemInput : input.getUserItems()) {
            UserItem userItem = userItems.get(userItemInput.getExternalAttributeId());

            OfferUserItem offerUserItem = OfferUserItem.builder()
                .userItem(userItem)
                .offer(offer)
                .quantity(userItemInput.getQuantity())
                .build();

            offerUserItems.add(offerUserItem);
        }

        return offerUserItems;
    }

    private List<UserItem> decreaseUserItemsQuantityOffered(HashMap<String, UserItem> userItems, MakeOfferInput input) {
        for (MakeOfferInput.UserItemInput userItemInput : input.getUserItems()) {
            UserItem userItem = userItems.get(userItemInput.getExternalAttributeId());
            userItem.setQuantity(userItem.getQuantity() - userItemInput.getQuantity());
        }

        return userItemRepository.persistAll(userItems.values().stream().toList());
    }

    private void persistOfferUserItems(List<OfferUserItem> offerUserItems) {
        offerUserItemRepository.persistAll(offerUserItems);
    }

    private void unlockUserWallet(DatabaseLock databaseLock) {
        databaseLockRepository.removeLock(databaseLock);
    }
}
