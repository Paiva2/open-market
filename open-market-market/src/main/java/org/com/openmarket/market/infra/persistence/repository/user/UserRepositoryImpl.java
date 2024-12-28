package org.com.openmarket.market.infra.persistence.repository.user;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.domain.interfaces.UserRepository;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.com.openmarket.market.infra.persistence.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserRepositoryOrm repository;

    @Override
    public Optional<User> findByExternalId(String externalId) {
        Optional<UserEntity> userEntity = repository.findByExternalId(externalId);
        if (userEntity.isEmpty()) return Optional.empty();

        return Optional.of(UserMapper.toDomain(userEntity.get()));
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = repository.save(UserMapper.toPersistence(user));
        return UserMapper.toDomain(userEntity);
    }
}
