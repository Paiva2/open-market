package org.com.openmarket.users.infra.persistence.repository.user;

import org.com.openmarket.users.infra.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryOrm extends JpaRepository<UserEntity, Long> {
}
