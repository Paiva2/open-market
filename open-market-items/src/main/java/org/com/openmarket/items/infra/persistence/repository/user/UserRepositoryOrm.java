package org.com.openmarket.items.infra.persistence.repository.user;

import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryOrm extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
