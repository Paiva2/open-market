package org.com.openmarket.items.application.gateway.controller.userItem;

import lombok.AllArgsConstructor;
import org.com.openmarket.items.application.gateway.controller.exception.ExternalIdMissingException;
import org.com.openmarket.items.core.domain.usecase.common.dto.PageableList;
import org.com.openmarket.items.core.domain.usecase.userItem.listUserItems.ListUserItemsUsecase;
import org.com.openmarket.items.core.domain.usecase.userItem.listUserItems.dto.UserItemOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/item")
public class UserItemController {
    private final ListUserItemsUsecase listUserItemsUsecase;

    @GetMapping("/list/user-item")
    public ResponseEntity<PageableList<UserItemOutput>> listUserItems(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(value = "size", required = false, defaultValue = "50") Integer size
    ) {
        Long subjectId = getIdFromToken(jwt);
        PageableList<UserItemOutput> output = listUserItemsUsecase.execute(subjectId, page, size);

        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    private Long getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("sub").split(":")[2];

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return Long.valueOf(externalIdString);
    }
}
