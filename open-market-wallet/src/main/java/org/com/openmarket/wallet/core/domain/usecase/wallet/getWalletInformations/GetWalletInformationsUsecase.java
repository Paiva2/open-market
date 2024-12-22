package org.com.openmarket.wallet.core.domain.usecase.wallet.getWalletInformations;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.domain.entity.view.WalletInformationsView;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.WalletNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getWalletInformations.dto.GetWalletInformationsOutput;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.core.interfaces.WalletInformationsViewRepository;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetWalletInformationsUsecase {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletInformationsViewRepository walletInformationsViewRepository;

    public GetWalletInformationsOutput execute(String externalId) {
        User user = findUser(externalId);
        Wallet wallet = findWallet(user.getId());

        WalletInformationsView walletInformationsView = findWalletInformations(wallet.getId());

        return GetWalletInformationsOutput.toOutput(walletInformationsView);
    }

    private User findUser(String externalId) {
        return userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);
    }

    private Wallet findWallet(UUID userId) {
        return walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new);
    }

    private WalletInformationsView findWalletInformations(UUID walletId) {
        return walletInformationsViewRepository.findByWalletId(walletId).orElseThrow(WalletNotFoundException::new);
    }
}
