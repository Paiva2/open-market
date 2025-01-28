package org.com.openmarket.users.infra.persistence.repository.user;

import org.com.openmarket.users.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryOrm extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
}
