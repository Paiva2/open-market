package org.com.openmarket.wallet.infra.persistence.mapper;

import org.com.openmarket.wallet.core.domain.entity.User;
import org.com.openmarket.wallet.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static User toDomain(UserEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        User user = new User();
        copyProperties(persistenceEntity, user);

        return user;
    }

    public static UserEntity toPersistence(User domainEntity) {
        if (domainEntity == null) return null;

        UserEntity userEntity = new UserEntity();
        copyProperties(domainEntity, userEntity);

        return userEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
