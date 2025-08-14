package com.nextcloudlab.kickytime.match.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;

    public MyMatchesResponse getMyParticipant(String cognitoSub) {

        List<MyMatchesResponse.MatchInfo> matches =
                matchParticipantRepository.findMatchParticipantByUserId(cognitoSub);

        long totalCount = matches.size();
        long upcomingCount = calculateUpcomingCount(matches);
        long completedCount = calculateCompletedCount(matches);

        MyMatchesResponse.Summary summary =
                new MyMatchesResponse.Summary(totalCount, upcomingCount, completedCount);

        return new MyMatchesResponse(summary, matches);
    }

    private long calculateUpcomingCount(List<MyMatchesResponse.MatchInfo> matches) {
        return matches.stream().filter(this::isUpcoming).count();
    }

    private long calculateCompletedCount(List<MyMatchesResponse.MatchInfo> matches) {
        return matches.stream().filter(this::isCompleted).count();
    }

    private boolean isUpcoming(MyMatchesResponse.MatchInfo match) {
        return match.matchStatus() == MatchStatus.FULL || match.matchStatus() == MatchStatus.OPEN;
    }

    private boolean isCompleted(MyMatchesResponse.MatchInfo match) {
        return match.matchStatus() == MatchStatus.CLOSED
                || match.matchStatus() == MatchStatus.CANCELLED;
    }
}
