package com.example.yourproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.yourproject.service.MatchingService;

import lombok.RequiredArgsConstructor;

/ * 관리자 전용 매치 삭제 API 컨트롤러
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchingAdminController {

    private final MatchingService matchingService;

    @DeleteMapping("/{matchId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchId) {
        matchingService.deleteMatchById(matchId);
        return ResponseEntity.noContent().build();  // 204 No Content
    }
}
