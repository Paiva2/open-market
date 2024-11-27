package org.com.openmarket.users.core.domain.usecase.user.getUserProfile;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.core.domain.entity.User;
import org.com.openmarket.users.core.domain.repository.UserRepository;
import org.com.openmarket.users.core.domain.usecase.common.exception.UserNotFoundException;
import org.com.openmarket.users.core.domain.usecase.user.getUserProfile.dto.GetUserProfileOutput;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class GetUserProfileUsecase {
    private final UserRepository userRepository;

    public GetUserProfileOutput execute(Long userId) {
        User user = findUser(userId);
        return mountOutput(user);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }

    private GetUserProfileOutput mountOutput(User user) {
        return GetUserProfileOutput.builder()
            .id(user.getId())
            .userName(user.getUserName())
            .email(user.getEmail())
            .build();
    }
}
