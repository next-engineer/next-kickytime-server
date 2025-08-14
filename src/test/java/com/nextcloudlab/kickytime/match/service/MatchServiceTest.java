package com.nextcloudlab.kickytime.match.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.nextcloudlab.kickytime.service.MatchingService;
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
    @DisplayName("매칭 삭제 성공")
    void deleteMatchByIdSuccess() {
        // given
        // 매칭 ID가 존재하는 경우를 가정
        given(matchRepository.existsById(testMatch.getId())).willReturn(true);
        // 삭제 메서드 호출 시 아무것도 하지 않도록 설정
        willDoNothing().given(matchRepository).deleteById(testMatch.getId());

        // when
        // 실제 매칭 삭제 메서드 호출
        MatchingService matchingService;
        matchingService.deleteMatchById(testMatch.getId());

        // then
        // `existsById`가 한 번 호출되었는지 검증
        verify(matchRepository, times(1)).existsById(testMatch.getId());
        // `deleteById`가 한 번 호출되었는지 검증
        verify(matchRepository, times(1)).deleteById(testMatch.getId());
    }

    @Test
    @DisplayName("매칭 삭제 실패 - 매칭이 존재하지 않을 경우")
    void deleteMatchByIdFailWhenNotExists() {
        // given
        Long invalidId = 999L;
        given(matchRepository.existsById(invalidId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> matchService.deleteMatchById(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 매치를 찾을 수 없습니다.");

        verify(matchRepository, times(1)).existsById(invalidId);
        verify(matchRepository, never()).deleteById(anyLong());
    }