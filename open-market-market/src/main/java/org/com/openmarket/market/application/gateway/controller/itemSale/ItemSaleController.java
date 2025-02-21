package org.com.openmarket.market.application.gateway.controller.itemSale;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.openmarket.market.application.gateway.controller.common.exception.ExternalIdMissingException;
import org.com.openmarket.market.domain.core.usecase.common.dto.PageableList;
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.InsertItemSaleUsecase;
import org.com.openmarket.market.domain.core.usecase.itemSale.insertItemSale.dto.InsertItemSaleInput;
import org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale.ListItemsOnSaleUsecase;
import org.com.openmarket.market.domain.core.usecase.itemSale.listItemsOnSale.dto.ListItemsOnSaleOutput;
import org.com.openmarket.market.domain.core.usecase.itemSale.removeItemSale.RemoveItemSaleUsecase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class ItemSaleController {
    private final InsertItemSaleUsecase insertItemSaleUsecase;
    private final RemoveItemSaleUsecase removeItemSaleUsecase;
    private final ListItemsOnSaleUsecase listItemsOnSaleUsecase;

    @PostMapping("/item-sale/insert")
    ResponseEntity<Void> insertItemForSale(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid InsertItemSaleInput input
    ) {
        String externalUserId = getIdFromToken(jwt);
        insertItemSaleUsecase.execute(externalUserId, input);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/item-sale/{itemSaleId}")
    ResponseEntity<Void> removeItemForSale(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable("itemSaleId") UUID itemSaleId
    ) {
        String externalUserId = getIdFromToken(jwt);
        removeItemSaleUsecase.execute(externalUserId, itemSaleId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/item-sale/list")
    ResponseEntity<PageableList<ListItemsOnSaleOutput>> listItemsForSale(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "5") Integer size,
        @RequestParam(value = "itemName", required = false) String itemName,
        @RequestParam(value = "categoryId", required = false) String categoryId,
        @RequestParam(value = "min", required = false) BigDecimal min,
        @RequestParam(value = "max", required = false) BigDecimal max
    ) {
        getIdFromToken(jwt);
        PageableList<ListItemsOnSaleOutput> output = listItemsOnSaleUsecase.execute(page, size, itemName, categoryId, min, max);

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
