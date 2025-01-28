package org.com.openmarket.wallet.core.domain.usecase.wallet.getBankAdminWalletId;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.WalletNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getBankAdminWalletId.dto.GetAdminWalletOutput;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class GetBankAdminWalletIdUsecase {
    private final static String BANK_ADM_EMAIL = "BANK_ADM_EMAIL";

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public GetAdminWalletOutput execute() {
        User user = findBankUser();

        if (!user.getEnabled()) {
            throw new UserNotFoundException();
        }

        Wallet wallet = findBankWallet(user.getId());

        return GetAdminWalletOutput.builder()
            .externalAdminId(user.getExternalId())
            .walletId(wallet.getId())
            .build();
    }

    private User findBankUser() {
        return userRepository.findByEmail(System.getenv(BANK_ADM_EMAIL)).orElseThrow(UserNotFoundException::new);
    }

    private Wallet findBankWallet(UUID id) {
        return walletRepository.findByUserId(id).orElseThrow(WalletNotFoundException::new);
    }
}
