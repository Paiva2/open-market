package org.com.openmarket.market.application.gateway.controller.offerUserItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.market.application.gateway.controller.common.exception.ExternalIdMissingException;
import org.com.openmarket.market.domain.core.usecase.offerUserItem.listOfferUserItemsByOffer.ListOffersUserItemByOfferUsecase;
import org.com.openmarket.market.domain.core.usecase.offerUserItem.listOfferUserItemsByOffer.dto.ListOffersUserItemByOfferOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/market")
public class OfferUserItemController {
    private final ListOffersUserItemByOfferUsecase listOffersUserItemByOfferUsecase;

    @GetMapping("/offer/{offerId}/items/list")
    ResponseEntity<List<ListOffersUserItemByOfferOutput>> listOfferUserItems(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable("offerId") UUID offerId
    ) {
        String userExternalId = getIdFromToken(jwt);
        List<ListOffersUserItemByOfferOutput> output = listOffersUserItemByOfferUsecase.execute(userExternalId, offerId);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private String getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("sub").split(":")[2];

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return externalIdString;
    }
}
