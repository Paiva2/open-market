package org.com.openmarket.market.infra.persistence.repository.userItem;

import org.com.openmarket.market.infra.persistence.entity.UserItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserItemRepositoryOrm extends JpaRepository<UserItemEntity, UserItemEntity.KeyId> {
    @Query("select ui from UserItemEntity ui " +
        "join fetch ui.user usr " +
        "join fetch ui.item itm " +
        "where usr.id = :userId " +
        "and itm.id = :itemId")
    Optional<UserItemEntity> findUserItem(@Param("userId") UUID userId, @Param("itemId") UUID itemId);
}
