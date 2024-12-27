package org.com.openmarket.items.core.domain.usecase.user.disableUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DisableUserUsecase {
    private final UserRepository userRepository;

    public void execute(Long externalUserId) {
        User user = findUser(externalUserId);

        user.setEnabled(false);
        persistUser(user);
    }

    private User findUser(Long externalUserId) {
        return userRepository.findByExternalId(externalUserId).orElseThrow(UserNotFoundException::new);
    }

    private void persistUser(User user) {
        userRepository.save(user);
    }
}
