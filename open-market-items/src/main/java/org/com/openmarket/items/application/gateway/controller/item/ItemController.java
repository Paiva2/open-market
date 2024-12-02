package org.com.openmarket.items.application.gateway.controller.item;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.application.gateway.controller.exception.ExternalIdMissingException;
import org.com.openmarket.items.application.gateway.controller.item.dto.CreateItemDTO;
import org.com.openmarket.items.core.domain.usecase.item.createItem.CreateItemUsecase;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/item")
@AllArgsConstructor
public class ItemController {
    private final CreateItemUsecase createItemUsecase;

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    @Transactional
    public ResponseEntity<CreateItemOutput> createItem(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CreateItemDTO dto) {
        Long subjectId = getIdFromToken(jwt);
        CreateItemOutput output = createItemUsecase.execute(subjectId, dto.toInput());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    private Long getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("externalId");

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return Long.valueOf(externalIdString);
    }
}
