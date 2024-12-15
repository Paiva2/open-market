package org.com.openmarket.wallet.core.domain.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.dto.RegisterUserInput;
import org.com.openmarket.wallet.core.domain.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterUserUsecase {
    private final UserRepository userRepository;

    public void execute(RegisterUserInput input) {
        checkUserAlreadyExists(input.getEmail());

        User user = fillUser(input);
        saveUser(user);
    }

    private void checkUserAlreadyExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new UserAlreadyExistsException();
        }
    }

    private User fillUser(RegisterUserInput input) {
        return User.builder()
            .externalId(input.getExternalId())
            .email(input.getEmail())
            .username(input.getUserName())
            .build();
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }
}
