package com.nextcloudlab.kickytime.match.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.nextcloudlab.kickytime.match.entity.MatchStatus;

public record MyMatchesResponse(Summary summary, List<MatchInfo> matches) {
    public record Summary(long totalCount, long upcomingCount, long completedCount) {}

    public static record MatchInfo(
            Long id,
            Long participantId,
            String location,
            LocalDateTime matchDateTime,
            int maxPlayers,
            long currentPlayers,
            LocalDateTime joinedAt,
            MatchStatus matchStatus) {}
}
