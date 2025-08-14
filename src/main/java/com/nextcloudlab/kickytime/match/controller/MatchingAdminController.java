package com.nextcloudlab.kickytime.match.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nextcloudlab.kickytime.match.service.MatchingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchingAdminController {

    private final MatchingService matchingService;

    @DeleteMapping("/{matchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchId) {
        matchingService.deleteMatchById(matchId);
        return ResponseEntity.noContent().build();
    }
}
