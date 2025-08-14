package com.nextcloudlab.kickytime.match.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import com.nextcloudlab.kickytime.match.dto.MatchCreateRequestDto;
import com.nextcloudlab.kickytime.match.dto.MatchResponseDto;
import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.service.MatchParticipantService;
import com.nextcloudlab.kickytime.match.service.MatchService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
@Tag(name = "경기 관련", description = "경기 개설, 참여, 취소 관련 API")
public class MatchController {

    private final MatchService matchService;
    private final MatchParticipantService matchParticipantService;

    // 전체 경기 목록 조회
    @GetMapping
    public ResponseEntity<List<MatchResponseDto>> getAllMatches() {
        List<MatchResponseDto> matches = matchService.getAllMatches();
        return ResponseEntity.ok(matches);
    }

    // 경기 개설
    @PostMapping
    public ResponseEntity<Void> createMatch(@RequestBody MatchCreateRequestDto requestDto) {
        matchService.createMatch(requestDto);
        return ResponseEntity.ok().build();
    }

    // 경기 참여 신청
    @PostMapping("/{matchId}/participants")
    public ResponseEntity<Void> joinMatch(@PathVariable Long matchId, @RequestParam Long userId) {
        matchService.joinMatch(matchId, userId);
        return ResponseEntity.ok().build();
    }

    // 경기 참여 취소
    @DeleteMapping("/{matchId}/participants")
    public ResponseEntity<Void> leaveMatch(@PathVariable Long matchId, @RequestParam Long userId) {
        matchService.leaveMatch(matchId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<MyMatchesResponse> getMyMatches(@AuthenticationPrincipal Jwt jwt) {
        String cognitoSub = jwt.getClaimAsString("sub");
        MyMatchesResponse response = matchParticipantService.getMyParticipant(cognitoSub);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{matchId}")
    public ResponseEntity<Void> deleteMatch(@PathVariable Long matchId) {
        matchService.deleteMatchById(matchId);
        return ResponseEntity.noContent().build();
    }
}
