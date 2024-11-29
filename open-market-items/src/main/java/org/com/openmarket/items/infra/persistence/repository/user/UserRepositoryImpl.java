package org.com.openmarket.items.infra.persistence.repository.user;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.core.domain.repository.UserRepository;
import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.com.openmarket.items.infra.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryOrm repository;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> user = repository.findByEmail(email);
        if (user.isEmpty()) return Optional.empty();

        return Optional.of(UserMapper.toDomain(user.get()));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = repository.save(UserMapper.toPersistence(user));

        return UserMapper.toDomain(userEntity);
    }
}
