package org.com.openmarket.users.infra.persistence.repository.user;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.core.domain.entity.User;
import org.com.openmarket.users.core.domain.repository.UserRepository;
import org.com.openmarket.users.infra.persistence.entity.UserEntity;
import org.com.openmarket.users.infra.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@AllArgsConstructor
@Component
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryOrm userRepositoryOrm;

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> userEntity = userRepositoryOrm.findById(id);
        if (userEntity.isEmpty()) return Optional.empty();

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<UserEntity> userEntity = userRepositoryOrm.findByEmail(email);
        if (userEntity.isEmpty()) return Optional.empty();

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userRepositoryOrm.save(UserMapper.toPersistence(user));
        return UserMapper.toDomain(userEntity);
    }
}
