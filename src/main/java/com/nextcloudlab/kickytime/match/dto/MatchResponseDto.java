package com.nextcloudlab.kickytime.match.dto;

import java.time.LocalDateTime;

import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "경기 개설 요청 데이터")
public class MatchResponseDto {

    @Schema(description = "경기 ID", example = "1", required = true)
    private Long id;

    @Schema(
            description = "경기 상태",
            example = "OPEN, FULL, CLOSED, CANCELED",
            required = false,
            defaultValue = "OPEN")
    private MatchStatus matchStatus;

    @Schema(description = "경기 일시", example = "2025-08-14T19:00:00", required = true)
    private LocalDateTime matchDateTime;

    @Schema(description = "경기 장소", example = "서울특별시 축구장", required = true)
    private String location;

    @Schema(description = "최대 참가자 수", example = "10", required = true)
    private Integer maxPlayers;

    private Integer currentParticipants;

    public MatchResponseDto(Match match) {
        this.id = match.getId();
        this.matchStatus = match.getMatchStatus();
        this.matchDateTime = match.getMatchDateTime();
        this.location = match.getLocation();
        this.maxPlayers = match.getMaxPlayers();
        this.currentParticipants = match.getCurrentParticipantCount();
    }
}
