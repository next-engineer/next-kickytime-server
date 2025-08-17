package com.nextcloudlab.kickytime.user.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nextcloudlab.kickytime.user.entity.RoleEnum;
import com.nextcloudlab.kickytime.user.repository.UserRepository;
import com.nextcloudlab.kickytime.user.service.CognitoBackfillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final CognitoBackfillService backfillService;
    private final UserRepository userRepository;

    @PostMapping("/backfill-cognito")
    public ResponseEntity<CognitoBackfillService.BackfillReport> backfillAll(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            throw new ResponseStatusException(BAD_REQUEST, "실행하려면 ?confirm=true 를 붙여주세요.");
        }

        String cognitoSub = (jwt != null) ? jwt.getClaimAsString("sub") : null;
        if (cognitoSub == null || cognitoSub.isBlank()) {
            throw new ResponseStatusException(UNAUTHORIZED, "유효한 인증 토큰이 필요합니다.");
        }

        boolean isAdmin =
                userRepository
                        .findByCognitoSub(cognitoSub)
                        .map(u -> u.getRole() == RoleEnum.ADMIN)
                        .orElse(false);

        if (!isAdmin) {
            throw new ResponseStatusException(FORBIDDEN, "관리자만 실행할 수 있습니다.");
        }

        var report = backfillService.backfillAllUsers();
        return ResponseEntity.ok(report);
    }
}
