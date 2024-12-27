package org.com.openmarket.wallet.core.domain.usecase.user.disableUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DisableUserUsecase {
    private final UserRepository userRepository;

    public void execute(String externalUserId) {
        User user = findUser(externalUserId);

        user.setEnabled(false);
        userRepository.save(user);
    }

    private User findUser(String externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }
}
