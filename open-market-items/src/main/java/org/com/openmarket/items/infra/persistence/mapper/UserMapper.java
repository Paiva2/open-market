package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

public class UserMapper {
    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) return null;

        User user = new User();
        convert(userEntity, user);

        return user;
    }

    public static UserEntity toPersistence(User user) {
        if (user == null) return null;

        UserEntity userEntity = new UserEntity();
        convert(user, userEntity);

        return userEntity;
    }

    private static void convert(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}