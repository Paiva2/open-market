package org.com.openmarket.items.core.domain.usecase.user.insertUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.interfaces.repository.UserRepository;
import org.com.openmarket.items.core.domain.usecase.common.exception.InvalidFieldException;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.dto.InsertUserInput;
import org.com.openmarket.items.core.domain.usecase.user.insertUser.exception.UserAlreadyExistsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class InsertUserUsecase {
    private final UserRepository userRepository;

    public void execute(InsertUserInput input) {
        validateFields(input);
        checkEmailExists(input.getEmail());

        User user = mountUser(input);
        persistUser(user);
    }

    private void validateFields(InsertUserInput input) {
        if (input.getExtId() == null) {
            throw new InvalidFieldException("extId");
        }

        if (input.getEmail() == null) {
            throw new InvalidFieldException("email");
        }

        if (input.getUsername() == null) {
            throw new InvalidFieldException("userName");
        }
    }

    private void checkEmailExists(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isPresent()) {
            throw new UserAlreadyExistsException(email);
        }
    }

    private User mountUser(InsertUserInput input) {
        return User.builder()
            .externalId(input.getExtId())
            .email(input.getEmail())
            .userName(input.getUsername())
            .enabled(true)
            .build();
    }

    private void persistUser(User user) {
        userRepository.save(user);
    }
}
