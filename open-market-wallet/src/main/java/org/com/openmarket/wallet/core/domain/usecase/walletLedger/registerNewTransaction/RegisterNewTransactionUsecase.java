package org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.domain.entity.WalletLedger;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.WalletNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.dto.RegisterNewTransactionInput;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception.InvalidTypeException;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.registerNewTransaction.exception.InvalidValueException;
import org.com.openmarket.wallet.core.enums.EnumTransactionType;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.core.interfaces.WalletLedgerRepository;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RegisterNewTransactionUsecase {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletLedgerRepository walletLedgerRepository;

    public void execute(RegisterNewTransactionInput input) {
        User user = findUser(input.getExternalUserId());
        Wallet userWallet = findWallet(user.getId());
        Wallet targetWallet = findTargetWallet(input.getTargetWalletId());

        checkValue(input.getValue());
        EnumTransactionType transactionType = checkType(input.getType());

        WalletLedger ledger = fillLedger(input, userWallet, targetWallet, transactionType);
        saveLedger(ledger);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private Wallet findWallet(UUID userId) {
        return walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new);
    }

    private Wallet findTargetWallet(UUID walletId) {
        Optional<Wallet> wallet = walletRepository.findById(walletId);

        if (wallet.isEmpty()) {
            throw new WalletNotFoundException("Target wallet not found!");
        }

        return wallet.get();
    }

    private void checkValue(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidValueException("Transaction value can't be less than zero: " + value);
        }
    }

    private EnumTransactionType checkType(String typeReceived) {
        try {
            return EnumTransactionType.valueOf(typeReceived);
        } catch (Exception ex) {
            throw new InvalidTypeException(typeReceived);
        }
    }

    private WalletLedger fillLedger(RegisterNewTransactionInput input, Wallet userWallet, Wallet targetWallet, EnumTransactionType transactionType) {
        return WalletLedger.builder()
            .wallet(userWallet)
            .targetWallet(targetWallet)
            .value(input.getValue().multiply(BigDecimal.valueOf(100)))
            .transactionType(transactionType)
            .description(input.getDescription())
            .build();
    }

    private void saveLedger(WalletLedger ledger) {
        walletLedgerRepository.save(ledger);
    }
}
