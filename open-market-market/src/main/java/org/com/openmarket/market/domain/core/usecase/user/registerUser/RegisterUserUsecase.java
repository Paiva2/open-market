package org.com.openmarket.market.domain.core.usecase.user.registerUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.core.usecase.user.registerUser.exception.UserAlreadyExistsException;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RegisterUserUsecase {
    private final UserRepository userRepository;

    public void execute(RegisterUserInput input) {
        checkUserExists(input);

        User userFilled = fillUser(input);
        persistUser(userFilled);
    }

    private void checkUserExists(RegisterUserInput input) {
        Optional<User> user = userRepository.findByExternalId(input.getExternalId());

        if (user.isPresent()) {
            throw new UserAlreadyExistsException(input.getExternalId());
        }
    }

    private User fillUser(RegisterUserInput input) {
        return User.builder()
            .externalId(input.getExternalId())
            .userName(input.getUserName())
            .email(input.getEmail())
            .enabled(true)
            .build();
    }

    private void persistUser(User user) {
        userRepository.save(user);
    }
}
