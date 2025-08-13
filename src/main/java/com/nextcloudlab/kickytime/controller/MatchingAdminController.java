package com.nextcloudlab.kickytime.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nextcloudlab.kickytime.service.MatchingService;

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
