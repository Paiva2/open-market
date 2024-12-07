package org.com.openmarket.items.application.gateway.controller.item;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.com.openmarket.items.application.gateway.controller.exception.ExternalIdMissingException;
import org.com.openmarket.items.application.gateway.controller.item.dto.CreateCategoryDTO;
import org.com.openmarket.items.application.gateway.controller.item.dto.CreateItemDTO;
import org.com.openmarket.items.application.gateway.controller.item.dto.UpdateItemDTO;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.CreateCategoryUsecase;
import org.com.openmarket.items.core.domain.usecase.category.createCategory.dto.CreateCategoryOutput;
import org.com.openmarket.items.core.domain.usecase.category.deleteCategory.DeleteCategoryUsecase;
import org.com.openmarket.items.core.domain.usecase.category.listCategories.ListCategoriesUsecase;
import org.com.openmarket.items.core.domain.usecase.category.listCategories.dto.ListCategoriesOutput;
import org.com.openmarket.items.core.domain.usecase.item.createItem.CreateItemUsecase;
import org.com.openmarket.items.core.domain.usecase.item.createItem.dto.CreateItemOutput;
import org.com.openmarket.items.core.domain.usecase.item.listItems.ListItemsUsecase;
import org.com.openmarket.items.core.domain.usecase.item.listItems.dto.ListItemsInput;
import org.com.openmarket.items.core.domain.usecase.item.listItems.dto.ListItemsOutput;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.UpdateItemUsecase;
import org.com.openmarket.items.core.domain.usecase.item.updateItem.dto.UpdateItemOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/item")
public class ItemController {
    private final CreateItemUsecase createItemUsecase;
    private final UpdateItemUsecase updateItemUsecase;
    private final ListItemsUsecase listItemsUsecase;
    private final ListCategoriesUsecase listCategoriesUsecase;
    private final CreateCategoryUsecase createCategoryUsecase;
    private final DeleteCategoryUsecase deleteCategoryUsecase;

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    @Transactional
    public ResponseEntity<CreateItemOutput> createItem(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CreateItemDTO dto) {
        Long subjectId = getIdFromToken(jwt);
        CreateItemOutput output = createItemUsecase.execute(subjectId, dto.toInput());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @PutMapping("/{itemId}/update")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    @Transactional
    public ResponseEntity<UpdateItemOutput> updateItem(@AuthenticationPrincipal Jwt jwt, @PathVariable("itemId") UUID itemId, @RequestBody @Valid UpdateItemDTO dto) {
        Long subjectId = getIdFromToken(jwt);
        UpdateItemOutput output = updateItemUsecase.execute(subjectId, dto.toInput(itemId));
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<ListItemsOutput> listItems(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "category", required = false) Long category,
        @RequestParam(name = "active", required = false) Boolean active,
        @RequestParam(value = "unique", required = false) Boolean unique,
        @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
        @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
        @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction
    ) {
        getIdFromToken(jwt);
        ListItemsOutput output = listItemsUsecase.execute(ListItemsInput.builder()
            .page(page)
            .size(size)
            .category(category)
            .active(active)
            .unique(unique)
            .maxPrice(maxPrice)
            .minPrice(minPrice)
            .name(name)
            .direction(direction)
            .build()
        );
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<ListCategoriesOutput> getCategories(
        @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "15") Integer size,
        @RequestParam(name = "name", required = false) String name
    ) {
        ListCategoriesOutput output = listCategoriesUsecase.execute(page, size, name);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @PostMapping("/category")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    public ResponseEntity<CreateCategoryOutput> createCategory(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid CreateCategoryDTO dto
    ) {
        Long subjectId = getIdFromToken(jwt);
        CreateCategoryOutput output = createCategoryUsecase.execute(subjectId, dto.getName());
        return new ResponseEntity<>(output, HttpStatus.CREATED);
    }

    @DeleteMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    @Transactional
    public ResponseEntity<CreateCategoryOutput> deleteCategory(
        @AuthenticationPrincipal Jwt jwt,
        @PathVariable("categoryId") Long categoryId
    ) {
        Long subjectId = getIdFromToken(jwt);
        deleteCategoryUsecase.execute(subjectId, categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private Long getIdFromToken(Jwt jwt) {
        String externalIdString = jwt.getClaimAsString("externalId");

        if (externalIdString == null) {
            throw new ExternalIdMissingException();
        }

        return Long.valueOf(externalIdString);
    }
}
