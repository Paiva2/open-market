package org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale;

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
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.dto.InsertItemSaleInput;
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.exception.UserItemQuantityException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTimeOnSale;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;

@Component
@AllArgsConstructor
public class InsertItemSaleUsecase {
    private final static String WALLET_DATABASE_NAME = "open-market-wallet-db";
    private final static String SYSTEM_WALLET_ID = "BANK_ADM_WALLET_ID";
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static Double MARKET_TAX = 0.02; // 2%
    private final static BigDecimal MAX_TAX_VALUE = new BigDecimal("1000000");

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final UserItemRepository userItemRepository;
    private final WalletRepository walletRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final MessageRepository messageRepository;

    private final DatabaseLockRepository databaseLockRepository;

    @Transactional
    public void execute(String externalUserId, InsertItemSaleInput input) {
        User user = findUser(externalUserId);
        Item item = findItem(input.getExternalItemId());

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        if (input.getOnlyOffers()) {
            input.setValue(BigDecimal.ZERO);
            input.setAcceptOffers(true);
        }

        BigDecimal tax = defineSaleTaxes(input, item);

        checkWalletLock(user);
        DatabaseLock walletLock = lockWallet(user);

        try {
            String systemWalletId = System.getenv(SYSTEM_WALLET_ID);

            if (systemWalletId == null) {
                throw new RuntimeException("System Wallet ID is null!");
            }

            UserWalletViewOutput walletView = findUserWallet();

            if (walletView.getBalance().compareTo(BigDecimal.ONE) < 1 || tax.compareTo(walletView.getBalance()) > 0) {
                throw new UserBalanceException("User has no balance available.");
            }

            if (!item.getActive()) {
                throw new ItemNotActiveException();
            } else if (item.getUnique()) {
                throw new UniqueItemException();
            }

            UserItem userItem = findUserItem(user, item, input.getExternalAttributeId());

            if (!input.getAcceptOffers()) {
                input.setOnlyOffers(false);
            }

            checkItemQuantity(userItem, input);
            checkSaleValue(input);

            ItemSale itemSale = fillItemSale(userItem, input);
            persistItemSale(itemSale);

            decreaseUserItemQuantity(userItem, input);
            sendUserItemDecreaseQuantityMessage(user, item, userItem);
            decreaseWalletTax(tax, user, UUID.fromString(systemWalletId));
            unlockWallet(walletLock);
        } catch (Exception exception) {
            unlockWallet(walletLock);
            throw exception;
        }
    }

    private void checkWalletLock(User user) {
        Optional<DatabaseLock> databaseLockEntity = databaseLockRepository.getLockByDatabaseAndUser(WALLET_DATABASE_NAME, user.getExternalId());

        if (databaseLockEntity.isPresent()) {
            throw new ConflictException("Another operation is being made on user wallet. Try again later!");
        }
    }

    private DatabaseLock lockWallet(User user) {
        return databaseLockRepository.saveLock(
            DatabaseLock.builder()
                .externalUserId(user.getExternalId())
                .databaseName(WALLET_DATABASE_NAME)
                .build()
        );
    }

    private void unlockWallet(DatabaseLock lock) {
        databaseLockRepository.removeLock(lock);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Item findItem(String externalItemId) {
        return itemRepository.findByExternalId(externalItemId).orElseThrow(ItemNotFoundException::new);
    }

    private UserWalletViewOutput findUserWallet() {
        return walletRepository.getUserWalletView();
    }

    private UserItem findUserItem(User user, Item item, String externalAttributeId) {
        Optional<UserItem> userItem = userItemRepository.getUserItemWithExternalAttributeId(user.getId(), item.getId(), externalAttributeId);

        if (userItem.isEmpty()) {
            throw new UserItemNotFoundException();
        }

        return userItem.get();
    }

    private void checkItemQuantity(UserItem userItem, InsertItemSaleInput input) {
        Long itemQuantityAvailability = userItem.getQuantity();

        if (itemQuantityAvailability < 1 || input.getQuantity() < 1 || (input.getQuantity() > itemQuantityAvailability)) {
            throw new UserItemQuantityException(userItem.getQuantity().toString());
        }
    }

    private void checkSaleValue(InsertItemSaleInput input) {
        if (input.getValue().compareTo(BigDecimal.ONE) < 0) {
            throw new InvalidFieldException("Invalid sale value. Value can't be less than 1.");
        }
    }

    private BigDecimal defineSaleTaxes(InsertItemSaleInput input, Item item) {
        BigDecimal saleValue = input.getOnlyOffers() ? item.getBaseSellingPrice() : input.getValue();

        if (input.getQuantity() > 1) {
            saleValue = saleValue.multiply(new BigDecimal(input.getQuantity()));
        }

        BigDecimal totalTax = saleValue.multiply(new BigDecimal(MARKET_TAX));

        if (totalTax.compareTo(MAX_TAX_VALUE) > 0) {
            totalTax = MAX_TAX_VALUE;
        }

        return totalTax;
    }

    private ItemSale fillItemSale(UserItem userItem, InsertItemSaleInput input) {
        return ItemSale.builder()
            .quantity(input.getQuantity())
            .value(input.getValue())
            .expirationDate(fillExpirationDate(input))
            .acceptOffers(input.getAcceptOffers())
            .onlyOffers(input.getOnlyOffers())
            .userItem(userItem)
            .build();
    }

    private void persistItemSale(ItemSale itemSale) {
        itemSaleRepository.save(itemSale);
    }

    private void decreaseUserItemQuantity(UserItem userItem, InsertItemSaleInput input) {
        userItem.setQuantity(userItem.getQuantity() - input.getQuantity());

        userItemRepository.persist(userItem);
    }

    private void sendUserItemDecreaseQuantityMessage(User user, Item item, UserItem userItem) {
        try {
            UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
                .externalUserId(user.getExternalId())
                .externalItemId(item.getExternalId())
                .externalAttributeId(userItem.getAttribute().getExternalId())
                .userItemInput(new UpdateUserItemMessageInput.UserItemInput(userItem.getQuantity()))
                .build();

            CommonMessageDTO messageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.UPDATED)
                .event(EnumMessageEvent.USER_ITEM_EVENT)
                .data(mapper.writeValueAsString(updateUserItemMessageInput))
                .build();

            messageRepository.sendMessage(ITEM_QUEUE, mapper.writeValueAsString(messageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void decreaseWalletTax(BigDecimal tax, User user, UUID systemWalletId) {
        try {
            WalletMessageInput walletMessageInput = WalletMessageInput.builder()
                .externalUserId(user.getExternalId())
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .targetWalletId(systemWalletId)
                    .description("Auto tax on inserting item for sale.")
                    .value(tax)
                    .type(EnumTransactionType.PAYMENT.name())
                    .build()
                ).build();

            CommonMessageDTO messageDTO = CommonMessageDTO.builder()
                .type(EnumMessageType.NEW_TRANSACTION)
                .event(EnumMessageEvent.WALLET_EVENT)
                .data(mapper.writeValueAsString(walletMessageInput))
                .build();

            messageRepository.sendMessage(WALLET_QUEUE, mapper.writeValueAsString(messageDTO));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Date fillExpirationDate(InsertItemSaleInput input) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        switch (input.getTimeOnSale()) {
            case TWELVE_HOURS -> calendar.add(Calendar.HOUR_OF_DAY, 12);
            case ONE_DAY -> calendar.add(Calendar.DATE, 1);
            case TWO_DAYS -> calendar.add(Calendar.DATE, 2);
            case FOUR_DAYS -> calendar.add(Calendar.DATE, 4);
            default ->
                throw new InvalidFieldException("Invalid timeOnSale. Valid inputs are: " + Arrays.toString(EnumTimeOnSale.values()));
        }

        return calendar.getTime();
    }
}
