package com.nextcloudlab.kickytime.user.controller;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nextcloudlab.kickytime.user.service.CognitoBackfillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final CognitoBackfillService backfillService;

    @PostMapping("/backfill-cognito")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CognitoBackfillService.BackfillReport> backfillAll(
            @RequestParam(defaultValue = "false") boolean confirm) {
        if (!confirm) {
            throw new ResponseStatusException(BAD_REQUEST, "실행하려면 ?confirm=true 를 붙여주세요.");
        }

        var report = backfillService.backfillAllUsers();
        return ResponseEntity.ok(report);
    }
}
