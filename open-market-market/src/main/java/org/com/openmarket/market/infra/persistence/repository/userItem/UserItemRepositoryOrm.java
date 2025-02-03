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
        "join fetch ui.attribute atr " +
        "where usr.id = :userId " +
        "and atr.externalId = :externalAttributeId " +
        "and itm.id = :itemId")
    Optional<UserItemEntity> findUserItemWithExternalAttribute(@Param("userId") UUID userId, @Param("itemId") UUID itemId, @Param("externalAttributeId") String externalAttributeId);

    @Query("""
            select ui from UserItemEntity ui
            join fetch ui.user usr
            join fetch ui.item itm
            join fetch ui.attribute atb
            where usr.id = :userId
            and itm.id = :itemId
            and atb.id = :attributeId
            and ui.quantity > 0
        """)
    Optional<UserItemEntity> findByUserAndItemIdAndAttributeIdWithQuantity(@Param("userId") UUID userId, @Param("itemId") UUID itemId, @Param("attributeId") UUID attributeId);
}
