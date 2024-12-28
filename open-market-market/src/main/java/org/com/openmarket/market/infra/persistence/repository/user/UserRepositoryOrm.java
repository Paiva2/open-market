package org.com.openmarket.market.infra.persistence.repository.user;

import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryOrm extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByExternalId(String externalId);
}
