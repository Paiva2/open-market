package org.com.openmarket.market.domain.interfaces;

import org.com.openmarket.market.domain.core.usecase.common.dto.GetAdminWalletOutput;
import org.com.openmarket.market.domain.core.usecase.common.dto.UserWalletViewOutput;

public interface WalletRepository {
    UserWalletViewOutput getUserWalletView(String authorizationToken);

    GetAdminWalletOutput getSystemBankAdminWalletId(String authorizationToken);
}
