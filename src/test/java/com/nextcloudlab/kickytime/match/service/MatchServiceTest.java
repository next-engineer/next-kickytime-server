package com.nextcloudlab.kickytime.match.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nextcloudlab.kickytime.match.controller.MatchResponseDto;
import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.entity.MatchStatus;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;
import com.nextcloudlab.kickytime.match.repository.MatchRepository;
import com.nextcloudlab.kickytime.user.entity.RoleEnum;
import com.nextcloudlab.kickytime.user.entity.User;
import com.nextcloudlab.kickytime.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @Mock private MatchRepository matchRepository;

    @Mock private MatchParticipantRepository participantRepository;

    @Mock private UserRepository userRepository;

    @InjectMocks private MatchService matchService;

    private User adminUser;
    private User regularUser;
    private Match testMatch;

    //    private MatchCreateRequestDto createRequestDto;

    @BeforeEach
    void setUp() {
        // 관리자 사용자
        adminUser = new User();
        adminUser.setId(1L);
        adminUser.setRole(RoleEnum.ADMIN);

        // 일반 사용자
        regularUser = new User();
        regularUser.setId(2L);
        regularUser.setRole(RoleEnum.USER);

        // 테스트용 경기
        testMatch = new Match();
        testMatch.setId(1L);
        testMatch.setMatchStatus(MatchStatus.OPEN);
        testMatch.setMatchDateTime(LocalDateTime.now().plusDays(1));
        testMatch.setLocation("서울 강남구");
        testMatch.setMaxPlayers(10);
        testMatch.setCreatedBy(adminUser);

        // 경기 생성 요청 DTO
        //        createRequestDto = new MatchCreateRequestDto();
        //        createRequestDto.setUserId(1L);
        //        createRequestDto.setMatchDateTime(LocalDateTime.now().plusDays(1));
        //        createRequestDto.setLocation("서울 강남구");
        //        createRequestDto.setMaxPlayers(10);
    }

    @Test
    @DisplayName("경기 목록 조회 - 성공")
    void getAllMatchesSuccess() {
        // given
        List<Match> matches = Arrays.asList(testMatch);
        given(matchRepository.findAllByOrderByMatchDateTimeDesc()).willReturn(matches);

        // when
        List<MatchResponseDto> result = matchService.getAllMatches();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(testMatch.getId());
        assertThat(result.get(0).getLocation()).isEqualTo(testMatch.getLocation());
        verify(matchRepository).findAllByOrderByMatchDateTimeDesc();
    }
}
