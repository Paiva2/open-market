package org.com.openmarket.wallet.application.gateway.controller.walletLedger;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import org.com.openmarket.wallet.application.gateway.controller.walletLedger.exception.ExternalIdMissingException;
import org.com.openmarket.wallet.core.domain.usecase.common.dto.WalletLedgerListPagedOutput;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.listWalletLedgers.ListWalletLedgersUsecase;
import org.com.openmarket.wallet.core.domain.usecase.walletLedger.listWalletLedgers.dto.ListWalletLedgersInput;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

@RestController
@AllArgsConstructor
@RequestMapping("/wallet/ledger")
public class WalletLedgerController {
    private final ListWalletLedgersUsecase listWalletLedgersUsecase;

    @GetMapping("/list")
    public ResponseEntity<WalletLedgerListPagedOutput> listPaged(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
        @RequestParam(value = "from", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
        @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to,
        @RequestParam(value = "min", required = false) @Min(value = 0) BigDecimal min,
        @RequestParam(value = "max", required = false) @Min(value = 0) BigDecimal max,
        @RequestParam(value = "direction", required = false, defaultValue = "DESC") String direction
    ) {
        String subjectId = getIdFromToken(jwt);
        WalletLedgerListPagedOutput output = listWalletLedgersUsecase.execute(ListWalletLedgersInput.builder()
            .externalId(subjectId)
            .page(page)
            .size(size)
            .from(from)
            .to(to)
            .min(min)
            .max(max)
            .direction(direction.toUpperCase(Locale.ROOT))
            .build()
        );
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
