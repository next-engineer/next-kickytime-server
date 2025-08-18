package com.nextcloudlab.kickytime.match.dto;

import java.time.LocalDateTime;

import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "경기 개설 요청 데이터")
public record MatchResponseDto(
        @Schema(description = "경기 ID", example = "1", required = true) Long id,
        @Schema(
                        description = "경기 상태",
                        example = "OPEN, FULL, CLOSED, CANCELED",
                        required = false,
                        defaultValue = "OPEN")
                MatchStatus matchStatus,
        @Schema(description = "경기 일시", example = "2025-08-14T19:00:00", required = true)
                LocalDateTime matchDateTime,
        @Schema(description = "경기 장소", example = "서울특별시 축구장", required = true) String location,
        @Schema(description = "최대 참가자 수", example = "10", required = true) Integer maxPlayers,
        Integer currentParticipants) {
    public static MatchResponseDto from(Match match) {
        return new MatchResponseDto(
                match.getId(),
                match.getMatchStatus(),
                match.getMatchDateTime(),
                match.getLocation(),
                match.getMaxPlayers(),
                match.getCurrentParticipantCount());
    }
}
