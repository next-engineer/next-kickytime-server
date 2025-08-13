package com.nextcloudlab.kickytime.match.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nextcloudlab.kickytime.match.service.MatchService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "경기 관련", description = "경기 개설, 참여, 취소 관련 API")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

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
}
