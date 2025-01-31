package org.com.openmarket.market.application.gateway.controller.offer;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.application.gateway.controller.common.exception.ExternalIdMissingException;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.offer.listOffersByItemSale.ListOffersByItemSaleUsecase;
import org.com.openmarket.market.domain.core.usecase.offer.listOffersByItemSale.dto.ListOffersByItemSaleOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class OfferController {
    private final ListOffersByItemSaleUsecase listOffersByItemSaleUsecase;

    @GetMapping("/offers/list/item-sale/{itemSaleId}")
    ResponseEntity<PageableList<ListOffersByItemSaleOutput>> getOffersByItemSale(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable("itemSaleId") UUID itemSaleId,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size
    ) {
        getIdFromToken(jwt);
        PageableList<ListOffersByItemSaleOutput> output = listOffersByItemSaleUsecase.execute(itemSaleId, page, size);

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
