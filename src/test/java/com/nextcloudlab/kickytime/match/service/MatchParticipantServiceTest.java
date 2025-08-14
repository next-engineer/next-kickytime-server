package com.nextcloudlab.kickytime.match.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MatchParticipantService 테스트")
class MatchParticipantServiceTest {

    @Mock private MatchParticipantRepository matchParticipantRepository;

    @InjectMocks private MatchParticipantService matchParticipantService;

    private String testCognitoSub;
    private List<MyMatchesResponse.MatchInfo> sampleMatches;
    private List<MyMatchesResponse.MatchInfo> realWorldMatches;

    @BeforeEach
    void setUp() {
        testCognitoSub = "test-cognito-sub-123";

        // 테스트용 매치 데이터 생성 (각 상태별로 2개씩)
        sampleMatches =
                Arrays.asList(
                        createMatchInfo(1L, MatchStatus.OPEN),
                        createMatchInfo(2L, MatchStatus.FULL),
                        createMatchInfo(3L, MatchStatus.CLOSED),
                        createMatchInfo(4L, MatchStatus.CANCELLED),
                        createMatchInfo(5L, MatchStatus.OPEN),
                        createMatchInfo(6L, MatchStatus.CLOSED));

        // SQL 데이터 기반 실제 시나리오 매치 데이터
        realWorldMatches =
                Arrays.asList(
                        createMatchInfoWithDetails(3L, MatchStatus.OPEN, "서울 OO풋살장 A코트", 10),
                        createMatchInfoWithDetails(4L, MatchStatus.CLOSED, "부산 XX풋살파크 B코트", 8),
                        createMatchInfoWithDetails(5L, MatchStatus.CANCELLED, "인천 YY스타디움 C코트", 12));
    }

    @Test
    @DisplayName("정상적인 매치 참가자 조회 - 모든 상태 포함")
    void getMyParticipantWithAllStatusesShouldReturnCorrectCounts() {
        // given
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(sampleMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result).isNotNull();
        assertThat(result.matches()).hasSize(6);

        MyMatchesResponse.Summary summary = result.summary();
        assertThat(summary.totalCount()).isEqualTo(6L);
        assertThat(summary.upcomingCount()).isEqualTo(3L); // OPEN(2) + FULL(1)
        assertThat(summary.completedCount()).isEqualTo(3L); // CLOSED(2) + CANCELLED(1)
    }

    @Test
    @DisplayName("OPEN 상태만 있는 경우")
    void getMyParticipantWithOnlyOpenMatchesShouldReturnCorrectCounts() {
        // given
        List<MyMatchesResponse.MatchInfo> openMatches =
                Arrays.asList(
                        createMatchInfo(1L, MatchStatus.OPEN),
                        createMatchInfo(2L, MatchStatus.OPEN));
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(openMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(2L);
        assertThat(result.summary().upcomingCount()).isEqualTo(2L);
        assertThat(result.summary().completedCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("FULL 상태만 있는 경우")
    void getMyParticipantWithOnlyFullMatchesShouldReturnCorrectCounts() {
        // given
        List<MyMatchesResponse.MatchInfo> fullMatches =
                Arrays.asList(
                        createMatchInfo(1L, MatchStatus.FULL),
                        createMatchInfo(2L, MatchStatus.FULL));
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(fullMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(2L);
        assertThat(result.summary().upcomingCount()).isEqualTo(2L);
        assertThat(result.summary().completedCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("CLOSED 상태만 있는 경우")
    void getMyParticipantWithOnlyClosedMatchesShouldReturnCorrectCounts() {
        // given
        List<MyMatchesResponse.MatchInfo> closedMatches =
                Arrays.asList(
                        createMatchInfo(1L, MatchStatus.CLOSED),
                        createMatchInfo(2L, MatchStatus.CLOSED));
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(closedMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(2L);
        assertThat(result.summary().upcomingCount()).isEqualTo(0L);
        assertThat(result.summary().completedCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("CANCELLED 상태만 있는 경우")
    void getMyParticipantWithOnlyCancelledMatchesShouldReturnCorrectCounts() {
        // given
        List<MyMatchesResponse.MatchInfo> cancelledMatches =
                Arrays.asList(
                        createMatchInfo(1L, MatchStatus.CANCELLED),
                        createMatchInfo(2L, MatchStatus.CANCELLED));
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(cancelledMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(2L);
        assertThat(result.summary().upcomingCount()).isEqualTo(0L);
        assertThat(result.summary().completedCount()).isEqualTo(2L);
    }

    @Test
    @DisplayName("매치가 없는 경우")
    void getMyParticipantWithNoMatchesShouldReturnEmptyResult() {
        // given
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(Collections.emptyList());

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result).isNotNull();
        assertThat(result.matches()).isEmpty();
        assertThat(result.summary().totalCount()).isEqualTo(0L);
        assertThat(result.summary().upcomingCount()).isEqualTo(0L);
        assertThat(result.summary().completedCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("단일 매치 - OPEN 상태")
    void getMyParticipantWithSingleOpenMatchShouldReturnCorrectCounts() {
        // given
        List<MyMatchesResponse.MatchInfo> singleMatch =
                Arrays.asList(createMatchInfo(1L, MatchStatus.OPEN));
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(singleMatch);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(1L);
        assertThat(result.summary().upcomingCount()).isEqualTo(1L);
        assertThat(result.summary().completedCount()).isEqualTo(0L);
    }

    @Test
    @DisplayName("매치 리스트가 정확히 반환되는지 확인")
    void getMyParticipantShouldReturnExactMatchList() {
        // given
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(sampleMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.matches()).isEqualTo(sampleMatches);
        assertThat(result.matches()).containsExactlyElementsOf(sampleMatches);
    }

    @Test
    @DisplayName("실제 데이터 시나리오 - SQL 기반 매치 참가자 조회")
    void getMyParticipantRealWorldScenarioShouldReturnCorrectCounts() {
        // given - user_id 3이 참가한 매치들 (OPEN, CLOSED, CANCELLED)
        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(realWorldMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result).isNotNull();
        assertThat(result.matches()).hasSize(3);

        MyMatchesResponse.Summary summary = result.summary();
        assertThat(summary.totalCount()).isEqualTo(3L);
        assertThat(summary.upcomingCount()).isEqualTo(1L); // OPEN(1)
        assertThat(summary.completedCount()).isEqualTo(2L); // CLOSED(1) + CANCELLED(1)

        // 매치 상세 정보 검증
        List<MyMatchesResponse.MatchInfo> matches = result.matches();
        assertThat(matches.get(0).location()).isEqualTo("서울 OO풋살장 A코트");
        assertThat(matches.get(1).location()).isEqualTo("부산 XX풋살파크 B코트");
        assertThat(matches.get(2).location()).isEqualTo("인천 YY스타디움 C코트");
    }

    @Test
    @DisplayName("풋살장별 매치 분포 테스트")
    void getMyParticipantLocationBasedMatchesShouldHandleCorrectly() {
        // given - 다양한 풋살장에서의 매치들
        List<MyMatchesResponse.MatchInfo> locationMatches =
                Arrays.asList(
                        createMatchInfoWithDetails(1L, MatchStatus.OPEN, "서울 OO풋살장 A코트", 10),
                        createMatchInfoWithDetails(2L, MatchStatus.FULL, "서울 OO풋살장 B코트", 8),
                        createMatchInfoWithDetails(3L, MatchStatus.CLOSED, "부산 XX풋살파크 A코트", 12),
                        createMatchInfoWithDetails(4L, MatchStatus.CANCELLED, "인천 YY스타디움 C코트", 16));

        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(locationMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(4L);
        assertThat(result.summary().upcomingCount()).isEqualTo(2L); // OPEN + FULL
        assertThat(result.summary().completedCount()).isEqualTo(2L); // CLOSED + CANCELLED

        // 서울 지역 매치가 2개인지 확인
        long seoulMatches =
                result.matches().stream().filter(match -> match.location().contains("서울")).count();
        assertThat(seoulMatches).isEqualTo(2L);
    }

    @Test
    @DisplayName("최대 참가자 수가 다른 매치들의 혼합 시나리오")
    void getMyParticipantMixedMaxPlayersMatchesShouldCalculateCorrectly() {
        // given - 다양한 최대 참가자 수를 가진 매치들
        List<MyMatchesResponse.MatchInfo> mixedMatches =
                Arrays.asList(
                        createMatchInfoWithDetails(1L, MatchStatus.OPEN, "소규모 풋살장", 6), // 소규모
                        createMatchInfoWithDetails(2L, MatchStatus.FULL, "중규모 풋살장", 10), // 중규모
                        createMatchInfoWithDetails(3L, MatchStatus.CLOSED, "대규모 풋살장", 16), // 대규모
                        createMatchInfoWithDetails(4L, MatchStatus.OPEN, "표준 풋살장", 12) // 표준
                        );

        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(mixedMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(4L);
        assertThat(result.summary().upcomingCount()).isEqualTo(3L); // OPEN(2) + FULL(1)
        assertThat(result.summary().completedCount()).isEqualTo(1L); // CLOSED(1)
    }

    @Test
    @DisplayName("시간 기반 매치 상태 검증 - 과거/미래 매치")
    void getMyParticipantTimeBasedMatchesShouldProcessCorrectly() {
        // given - 시간 관련 특성을 가진 매치들 (과거 날짜는 CLOSED/CANCELLED, 미래 날짜는 OPEN/FULL)
        List<MyMatchesResponse.MatchInfo> timeBasedMatches =
                Arrays.asList(
                        createMatchInfoWithDetails(
                                1L, MatchStatus.CLOSED, "과거 매치 1", 10), // 과거 - 종료됨
                        createMatchInfoWithDetails(
                                2L, MatchStatus.CANCELLED, "취소된 매치", 8), // 과거 - 취소됨
                        createMatchInfoWithDetails(3L, MatchStatus.OPEN, "미래 매치 1", 12), // 미래 - 모집중
                        createMatchInfoWithDetails(
                                4L, MatchStatus.FULL, "미래 매치 2", 10), // 미래 - 모집완료
                        createMatchInfoWithDetails(5L, MatchStatus.OPEN, "미래 매치 3", 14) // 미래 - 모집중
                        );

        when(matchParticipantRepository.findMatchParticipantByUserId(testCognitoSub))
                .thenReturn(timeBasedMatches);

        // when
        MyMatchesResponse result = matchParticipantService.getMyParticipant(testCognitoSub);

        // then
        assertThat(result.summary().totalCount()).isEqualTo(5L);
        assertThat(result.summary().upcomingCount()).isEqualTo(3L); // OPEN(2) + FULL(1)
        assertThat(result.summary().completedCount()).isEqualTo(2L); // CLOSED(1) + CANCELLED(1)
    }

    // 테스트 헬퍼 메서드
    private MyMatchesResponse.MatchInfo createMatchInfo(Long matchId, MatchStatus status) {
        return new MyMatchesResponse.MatchInfo(
                matchId,
                100L + matchId, // participantId
                "Test Location " + matchId,
                LocalDateTime.now().plusDays(matchId), // matchTime
                10, // maxPlayers
                5L, // currentPlayers
                LocalDateTime.now().minusDays(1), // joinedAt
                status);
    }

    // SQL 데이터 기반 실제 시나리오용 헬퍼 메서드
    private MyMatchesResponse.MatchInfo createMatchInfoWithDetails(
            Long matchId, MatchStatus status, String location, Integer maxPlayers) {

        LocalDateTime baseTime = LocalDateTime.now();
        LocalDateTime matchTime =
                switch (status) {
                    case OPEN, FULL -> baseTime.plusDays(2); // 미래 매치
                    case CLOSED, CANCELLED -> baseTime.minusDays(1); // 과거 매치
                };

        return new MyMatchesResponse.MatchInfo(
                matchId,
                200L + matchId, // participantId
                location,
                matchTime,
                maxPlayers,
                status == MatchStatus.FULL ? maxPlayers : maxPlayers - 2, // currentPlayers
                baseTime.minusDays(3), // joinedAt (3일 전 참가)
                status);
    }
}
