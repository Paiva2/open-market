package org.com.openmarket.users.application.gateway.controller.user;

import lombok.AllArgsConstructor;
import org.com.openmarket.users.application.gateway.controller.exception.ExternalIdMissingException;
import org.com.openmarket.users.core.domain.usecase.user.findUser.FindUserUsecase;
import org.com.openmarket.users.core.domain.usecase.user.findUser.dto.FindUserProfileOutput;
import org.com.openmarket.users.core.domain.usecase.user.getUserProfile.GetUserProfileUsecase;
import org.com.openmarket.users.core.domain.usecase.user.getUserProfile.dto.GetUserProfileOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    private final GetUserProfileUsecase getUserProfileUsecase;
    private final FindUserUsecase findUserUsecase;

    @GetMapping("/health")
    @PreAuthorize("hasAnyAuthority('ROLE_admin')")
    public String healthCheck() {
        return "USER SERVICE: OK";
    }

    @GetMapping("/profile")
    public ResponseEntity<GetUserProfileOutput> getProfile(@AuthenticationPrincipal Jwt jwt) {
        Long id = getIdFromToken(jwt);
        GetUserProfileOutput output = getUserProfileUsecase.execute(id);
        return new ResponseEntity<>(output, HttpStatus.OK);
    }

    @GetMapping("/find/{userId}")
    public ResponseEntity<FindUserProfileOutput> findUser(@PathVariable("userId") Long userId) {
        FindUserProfileOutput output = findUserUsecase.execute(userId);
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
