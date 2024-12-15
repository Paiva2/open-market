package org.com.openmarket.wallet.infra.persistence.repository.user;

import org.com.openmarket.wallet.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryOrm extends JpaRepository<UserEntity, UUID> {
    @Query("select usr from UserEntity usr where usr.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);
}
