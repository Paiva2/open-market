package org.com.openmarket.market.infra.persistence.mapper;

import org.com.openmarket.market.domain.core.entity.User;
import org.com.openmarket.market.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        if (entity == null) return null;

        User user = new User();
        copyProperties(entity, user);

        return user;
    }

    public static UserEntity toPersistence(User entity) {
        if (entity == null) return null;

        UserEntity user = new UserEntity();
        copyProperties(entity, user);

        return user;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
