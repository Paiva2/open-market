package org.com.openmarket.wallet.core.domain.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.dto.RegisterUserInput;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.core.interfaces.WalletRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterUserUsecase {
    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public void execute(RegisterUserInput input) {
        checkUserAlreadyExists(input.getEmail());

        User user = fillUser(input);
        user = saveUser(user);

        Wallet wallet = fillWallet(user);
        saveWallet(wallet);
    }

    private void checkUserAlreadyExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }

    private User fillUser(RegisterUserInput input) {
        return User.builder()
            .externalId(input.getExtId())
            .email(input.getEmail())
            .username(input.getUsername())
            .enabled(true)
            .build();
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private Wallet fillWallet(User user) {
        return Wallet.builder()
            .user(user)
            .build();
    }

    private void saveWallet(Wallet wallet) {
        walletRepository.save(wallet);
    }
}
