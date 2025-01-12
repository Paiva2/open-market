package org.com.openmarket.market.application.gateway.controller.itemSale;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.application.gateway.controller.common.exception.ExternalIdMissingException;
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.InsertItemSaleUsecase;
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.dto.InsertItemSaleInput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class ItemSaleController {
    private final InsertItemSaleUsecase insertItemSaleUsecase;

    @PostMapping("/item-sale/{externalItemId}")
    ResponseEntity<Void> insertItemForSale(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable("externalItemId") String externalItemId,
        @RequestBody @Valid InsertItemSaleInput input
    ) {
        String externalUserId = getIdFromToken(jwt);
        insertItemSaleUsecase.execute(externalUserId, externalItemId, input, jwt.getTokenValue());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private String getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("externalId");

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return externalIdString;
    }
}
