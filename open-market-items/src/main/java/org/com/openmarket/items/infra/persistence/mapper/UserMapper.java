package org.com.openmarket.items.infra.persistence.mapper;

import org.com.openmarket.items.core.domain.entity.ItemAlteration;
import org.com.openmarket.items.core.domain.entity.User;
import org.com.openmarket.items.infra.persistence.entity.ItemAlterationEntity;
import org.com.openmarket.items.infra.persistence.entity.UserEntity;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {
    public static User toDomain(UserEntity userEntity) {
        if (userEntity == null) return null;

        User user = new User();
        copyProperties(userEntity, user);

        if (userEntity.getItemAlterations() != null) {
            List<ItemAlteration> alterations = new ArrayList<>();

            for (ItemAlterationEntity itemAlterationEntity : userEntity.getItemAlterations()) {
                ItemAlteration itemAlteration = ItemAlterationMapper.toDomain(itemAlterationEntity);
                alterations.add(itemAlteration);
            }

            user.setItemAlterations(alterations);
        }

        return user;
    }

    public static UserEntity toPersistence(User user) {
        if (user == null) return null;

        UserEntity userEntity = new UserEntity();
        copyProperties(user, userEntity);

        return userEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}