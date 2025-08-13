package com.nextcloudlab.kickytime.match.service;

import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchParticipantService {

    private final MatchParticipantRepository matchParticipantRepository;

    public MyMatchesResponse getMyParticipations(String cognitoSub) {
        // 매치 리스트 조회
        List<MyMatchesResponse.MatchInfo> matches =
                matchParticipantRepository.findMatchParticipantByUserId(cognitoSub);

        // Summary 데이터 계산
        long totalCount = matches.size();
        long upcomingCount = calculateUpcomingCount(matches);
        long completedCount = calculateCompletedCount(matches);

        MyMatchesResponse.Summary summary =
                new MyMatchesResponse.Summary(totalCount, upcomingCount, completedCount);

        return new MyMatchesResponse(summary, matches);
    }

    private long calculateUpcomingCount(List<MyMatchesResponse.MatchInfo> matches) {
        LocalDateTime now = LocalDateTime.now();
        return matches.stream()
                .filter(match -> isUpcoming(match, now))
                .count();
    }

    private long calculateCompletedCount(List<MyMatchesResponse.MatchInfo> matches) {
        LocalDateTime now = LocalDateTime.now();
        return matches.stream()
                .filter(match -> isCompleted(match, now))
                .count();
    }

    private boolean isUpcoming(MyMatchesResponse.MatchInfo match, LocalDateTime now) {
        // 매치 시간이 현재보다 미래이고, 취소되지 않은 매치
        return match.matchTime().isAfter(now) &&
                match.matchStatus() != MatchStatus.CANCELLED;
    }

    private boolean isCompleted(MyMatchesResponse.MatchInfo match, LocalDateTime now) {
        // 매치 시간이 현재보다 과거이거나, CLOSED 상태이거나, 취소된 매치
        return match.matchTime().isBefore(now) ||
                match.matchStatus() == MatchStatus.CLOSED ||
                match.matchStatus() == MatchStatus.CANCELLED;
    }
}
