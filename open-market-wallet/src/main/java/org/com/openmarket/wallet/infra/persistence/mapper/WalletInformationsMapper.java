package org.com.openmarket.wallet.infra.persistence.mapper;

import org.com.openmarket.wallet.core.domain.entity.view.WalletInformationsView;
import org.com.openmarket.wallet.infra.persistence.entity.view.WalletInformationsViewEntity;
import org.springframework.beans.BeanUtils;

public class WalletInformationsMapper {
    public static WalletInformationsView toDomain(WalletInformationsViewEntity persistenceEntity) {
        if (persistenceEntity == null) return null;

        WalletInformationsView walletInformationsView = new WalletInformationsView();
        copyProperties(persistenceEntity, walletInformationsView);

        return walletInformationsView;
    }

    private static void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
