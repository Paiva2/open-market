package org.com.openmarket.items.infra.persistence.repository.userItem;

import org.com.openmarket.items.infra.persistence.entity.UserItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserItemRepositoryOrm extends JpaRepository<UserItemEntity, UserItemEntity.KeyId> {
    @Query("select uit from UserItemEntity uit " +
        "join fetch uit.user usr " +
        "join fetch uit.item itm " +
        "join fetch uit.attribute atr " +
        "where usr.id = :userId " +
        "and atr.id = :attributeId " +
        "and itm.id = :itemId")
    Optional<UserItemEntity> findUserItem(@Param("userId") UUID userId, @Param("itemId") UUID itemId, @Param("attributeId") UUID attributeId);

    @Modifying
    @Query("delete from UserItemEntity where id = :id")
    void remove(@Param("id") UserItemEntity.KeyId id);
}
