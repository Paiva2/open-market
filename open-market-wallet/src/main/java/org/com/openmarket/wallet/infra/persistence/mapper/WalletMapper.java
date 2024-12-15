package org.com.openmarket.wallet.infra.persistence.mapper;

import org.com.openmarket.wallet.core.domain.entity.Wallet;
import org.com.openmarket.wallet.infra.persistence.entity.UserEntity;
import org.com.openmarket.wallet.infra.persistence.entity.WalletEntity;
import org.springframework.beans.BeanUtils;

public class WalletMapper {
    public static Wallet toDomain(WalletEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        Wallet wallet = new Wallet();
        copyProperties(persistenceEntity, wallet);

        return wallet;
    }

    public static WalletEntity toPersistence(Wallet domainEntity) {
        if (domainEntity == null) return null;

        WalletEntity walletEntity = new WalletEntity();
        copyProperties(domainEntity, walletEntity);

        if (domainEntity.getUser() != null) {
            UserEntity userEntity = new UserEntity();
            copyProperties(domainEntity.getUser(), userEntity);

            walletEntity.setUser(userEntity);
        }

        return walletEntity;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
