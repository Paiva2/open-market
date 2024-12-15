package org.com.openmarket.wallet.infra.persistence.repository.user;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.core.interfaces.UserRepository;
import org.com.openmarket.wallet.infra.persistence.entity.UserEntity;
import org.com.openmarket.wallet.infra.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryOrm repository;

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = repository.findByEmail(email);
        if (userEntity.isEmpty()) return Optional.empty();
        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public User save(User user) {
        UserEntity userSaved = repository.save(UserMapper.toPersistence(user));
        return UserMapper.toDomain(userSaved);
    }
}
