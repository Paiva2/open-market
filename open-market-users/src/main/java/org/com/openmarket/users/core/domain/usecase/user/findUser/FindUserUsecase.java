package org.com.openmarket.users.core.domain.usecase.user.findUser;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.core.domain.entity.User;
import org.com.openmarket.users.core.domain.repository.UserRepository;
import org.com.openmarket.users.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.users.core.domain.usecase.user.findUser.dto.FindUserProfileOutput;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class FindUserUsecase {
    private final UserRepository userRepository;

    public FindUserProfileOutput execute(Long userId) {
        User user = findUser(userId);
        return mountOutput(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private FindUserProfileOutput mountOutput(User user) {
        return FindUserProfileOutput.builder()
            .id(user.getId())
            .userName(user.getUserName())
            .email(user.getEmail())
            .build();
    }
}
