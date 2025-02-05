package org.com.openmarket.market.domain.core.usecase.itemSale.removeItemSale;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.ItemSale;
import org.com.openmarket.market.domain.core.entity.Offer;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.entity.UserItem;
import org.com.openmarket.market.domain.core.usecase.common.dto.CommonMessageDTO;
import org.com.openmarket.market.domain.core.usecase.common.dto.UpdateUserItemMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;
import org.com.openmarket.market.domain.core.usecase.common.dto.WalletMessageInput;
import org.com.openmarket.market.domain.core.usecase.common.exception.ItemSaleNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserDisabledException;
import org.com.openmarket.market.domain.core.usecase.common.exception.UserItemNotFoundException;
import org.com.openmarket.market.domain.core.usecase.common.exception.core.ForbiddenException;
import org.com.openmarket.market.domain.enumeration.EnumMessageEvent;
import org.com.openmarket.market.domain.enumeration.EnumMessageType;
import org.com.openmarket.market.domain.enumeration.EnumTransactionType;
import org.com.openmarket.market.domain.interfaces.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.com.openmarket.market.application.config.constants.QueueConstants.Item.ITEM_QUEUE;
import static org.com.openmarket.market.application.config.constants.QueueConstants.Wallet.WALLET_QUEUE;

@Service
@AllArgsConstructor
public class RemoveItemSaleUsecase {
    private final static String SYSTEM_BANK_EXTERNAL_ID = "BANK_ADM_EXTERNAL_ID";
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static Double MARKET_TAX = 0.02; // 2%

    private final UserRepository userRepository;
    private final ItemSaleRepository itemSaleRepository;
    private final UserItemRepository userItemRepository;
    private final WalletRepository walletRepository;
    private final MessageRepository messageRepository;
    private final OfferRepository offerRepository;
    private final OfferUserItemRepository offerUserItemRepository;

    @Transactional
    public void execute(String authorizationToken, String externalId, UUID itemSaleId) {
        User user = findUser(externalId);

        if (!user.getEnabled()) {
            throw new UserDisabledException();
        }

        String systemWalletId = System.getenv(SYSTEM_BANK_EXTERNAL_ID);

        if (systemWalletId == null) {
            throw new RuntimeException("System wallet id is null!");
        }

        ItemSale itemSale = findItemSale(itemSaleId);
        UserItem userItem = itemSale.getUserItem();

        checkItemSaleUser(userItem, user);

        Long newQuantity = restoreUserItemQuantity(userItem, itemSale.getQuantity());

        removeItemSaleOffers(itemSale);
        removeItemSale(itemSale);

        UserWalletViewOutput userWallet = findUserWallet(authorizationToken);

        restoreUserWalletTaxValueMessage(userWallet, itemSale, systemWalletId);
        restoreUserItemQuantityMessage(userItem, newQuantity);
    }

    private User findUser(String externalId) {
        return userRepository.findByExternalId(externalId).orElseThrow(UserItemNotFoundException::new);
    }

    private ItemSale findItemSale(UUID itemSaleId) {
        return itemSaleRepository.findByIdWithDeps(itemSaleId).orElseThrow(ItemSaleNotFoundException::new);
    }

    private void checkItemSaleUser(UserItem userItem, User user) {
        if (!userItem.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("Item on sale do not belong to current user!");
        }
    }

    private void removeItemSaleOffers(ItemSale itemSale) {
        List<Offer> offers = offerRepository.getItemSaleOffers(itemSale.getId());

        if (offers == null || offers.isEmpty()) return;

        removeAllOfferUsersItems(offers);

        offerRepository.removeOffers(offers);
    }

    private void removeAllOfferUsersItems(List<Offer> offers) {
        List<UUID> offersIds = offers.stream().map(Offer::getId).toList();

        offerUserItemRepository.removeOfferUserItemByOfferIds(offersIds);
    }

    private void removeItemSale(ItemSale itemSale) {
        itemSaleRepository.remove(itemSale.getId());
    }

    private UserWalletViewOutput findUserWallet(String authorizationToken) {
        return walletRepository.getUserWalletView(authorizationToken);
    }

    private void restoreUserItemQuantityMessage(UserItem userItem, Long quantity) {
        try {
            UpdateUserItemMessageInput updateUserItemMessageInput = UpdateUserItemMessageInput.builder()
                .externalUserId(userItem.getUser().getExternalId())
                .externalItemId(userItem.getItem().getExternalId())
                .externalAttributeId(userItem.getAttribute().getExternalId())
                .userItemInput(new UpdateUserItemMessageInput.UserItemInput(quantity))
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

    private void restoreUserWalletTaxValueMessage(UserWalletViewOutput userWallet, ItemSale itemSale, String externalBankAdminId) {
        BigDecimal totalSaleValue = itemSale.getValue().multiply(new BigDecimal(itemSale.getQuantity()));
        BigDecimal totalSaleTax = totalSaleValue.multiply(new BigDecimal(MARKET_TAX));

        try {
            WalletMessageInput walletMessageInput = WalletMessageInput.builder()
                .externalUserId(externalBankAdminId)
                .transaction(WalletMessageInput.NewTransaction.builder()
                    .targetWalletId(userWallet.getId())
                    .description("Tax retrieval.")
                    .value(totalSaleTax)
                    .type(EnumTransactionType.DEPOSIT.name())
                    .build()
                ).build();

            CommonMessageDTO commonMessage = CommonMessageDTO.builder()
                .event(EnumMessageEvent.WALLET_EVENT)
                .type(EnumMessageType.NEW_TRANSACTION)
                .data(mapper.writeValueAsString(walletMessageInput))
                .build();

            messageRepository.sendMessage(WALLET_QUEUE, mapper.writeValueAsString(commonMessage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Long restoreUserItemQuantity(UserItem userItem, Long quantity) {
        userItem.setQuantity(userItem.getQuantity() + quantity);

        if (quantity > 0) {
            userItemRepository.persist(userItem);
        }

        return userItem.getQuantity();
    }
}
