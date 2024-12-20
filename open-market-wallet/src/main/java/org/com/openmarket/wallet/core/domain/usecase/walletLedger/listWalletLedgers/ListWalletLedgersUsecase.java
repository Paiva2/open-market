package org.com.openmarket.wallet.core.domain.usecase.walletLedger.listWalletLedgers;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.domain.usecase.common.dto.WalletLedgerListPagedOutput;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.InvalidFieldException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.WalletNotFoundException;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.listWalletLedgers.dto.ListWalletLedgersInput;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.core.interfaces.WalletLedgerRepository;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ListWalletLedgersUsecase {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletLedgerRepository walletLedgerRepository;

    public WalletLedgerListPagedOutput execute(ListWalletLedgersInput input) {
        User user = findUser(input.getExternalId());
        Wallet wallet = findWallet(user.getId());
        setFilterDefaults(input);

        WalletLedgerListPagedOutput walletLedgers = findLedgers(wallet.getId(), input);

        return walletLedgers;
    }

    private User findUser(String externalId) {
        return userRepository.findByExternalId(externalId).orElseThrow(UserNotFoundException::new);
    }

    private Wallet findWallet(UUID userId) {
        return walletRepository.findByUserId(userId).orElseThrow(WalletNotFoundException::new);
    }

    private void setFilterDefaults(ListWalletLedgersInput input) {
        if (input.getPage() < 1) {
            input.setPage(1);
        }

        if (input.getSize() < 5) {
            input.setSize(5);
        } else if (input.getSize() > 50) {
            input.setSize(50);
        }

        if (input.getFrom() != null && input.getTo() != null && input.getFrom().after(input.getTo())) {
            throw new InvalidFieldException("From can't be after to.");
        }

        if (input.getMin() != null && input.getMax() != null && input.getMax().compareTo(input.getMin()) < 0) {
            throw new InvalidFieldException("Max value can't be less than Min value.");
        }
    }

    private WalletLedgerListPagedOutput findLedgers(UUID walletId, ListWalletLedgersInput input) {
        return walletLedgerRepository.listPaged(walletId, input.getPage(), input.getSize(), input.getFrom(), input.getTo(), input.getMax(), input.getMin(), input.getDirection());
    }
}
