package com.nextcloudlab.kickytime.match.dto;

import com.nextcloudlab.kickytime.match.entity.MatchStatus;

import java.time.LocalDateTime;
import java.util.List;

public record MyMatchesResponse(
        Summary summary,
        List<MatchInfo> matches
) {
    public record Summary(
            long totalCount,
            long upcomingCount,
            long completedCount
    ) {}

    public record MatchInfo(
            Long matchId,
            Long participantId,
            String location,
            LocalDateTime matchTime,
            int maxPlayers,
            long currentPlayers,
            LocalDateTime joinedAt,
            MatchStatus matchStatus
    ) {}
}