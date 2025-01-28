package org.com.openmarket.wallet.application.gateway.controller.wallet;

import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.application.gateway.controller.walletLedger.exception.ExternalIdMissingException;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getBankAdminWalletId.GetBankAdminWalletIdUsecase;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getBankAdminWalletId.dto.GetAdminWalletOutput;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getWalletInformations.GetWalletInformationsUsecase;
import org.com.openmarket.wallet.core.domain.usecase.wallet.getWalletInformations.dto.GetWalletInformationsOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/wallet")
public class WalletController {
    private final GetWalletInformationsUsecase getWalletInformationsUsecase;
    private final GetBankAdminWalletIdUsecase getBankAdminWalletIdUsecase;

    @GetMapping("/info")
    public ResponseEntity<GetWalletInformationsOutput> getInformations(
        @AuthenticationPrincipal Jwt jwt
    ) {
        String subjectToken = getIdFromToken(jwt);
        GetWalletInformationsOutput output = getWalletInformationsUsecase.execute(subjectToken);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/info/system/bank")
    public ResponseEntity<GetAdminWalletOutput> getSystemBankId(
        @AuthenticationPrincipal Jwt jwt
    ) {
        getIdFromToken(jwt);
        GetAdminWalletOutput output = getBankAdminWalletIdUsecase.execute();
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private String getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("externalId");

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return externalIdString;
    }
}
