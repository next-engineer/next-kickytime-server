package com.nextcloudlab.kickytime.match.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nextcloudlab.kickytime.match.dto.MatchCreateRequestDto;
import com.nextcloudlab.kickytime.match.dto.MatchResponseDto;
import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.entity.MatchParticipant;
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

    private MatchCreateRequestDto createRequestDto;

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
        createRequestDto = new MatchCreateRequestDto();
        createRequestDto.setUserId(1L);
        createRequestDto.setMatchDateTime(LocalDateTime.now().plusDays(1));
        createRequestDto.setLocation("서울 강남구");
        createRequestDto.setMaxPlayers(10);
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

    @Test
    @DisplayName("경기 개설 - 성공")
    void createMatchSuccess() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.of(adminUser));
        given(matchRepository.save(any(Match.class))).willReturn(testMatch);

        // when
        assertDoesNotThrow(() -> matchService.createMatch(createRequestDto));

        // then
        verify(userRepository).findById(1L);
        verify(matchRepository).save(any(Match.class));
    }

    @Test
    @DisplayName("경기 개설 - 사용자 없음 예외")
    void createMatchUserNotFound() {
        // given
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(createRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("경기 개설 - 관리자 권한 없음 예외")
    void createMatchNotAdminUser() {
        // given
        createRequestDto.setUserId(2L);
        given(userRepository.findById(2L)).willReturn(Optional.of(regularUser));

        // when & then
        assertThatThrownBy(() -> matchService.createMatch(createRequestDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("관리자 권한이 있는 사용자만 경기를 개설할 수 있습니다.");
    }

    @Test
    @DisplayName("경기 참여 - 성공")
    void joinMatchSuccess() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(userRepository.findById(2L)).willReturn(Optional.of(regularUser));
        given(participantRepository.findByMatchIdAndUserId(1L, 2L)).willReturn(Optional.empty());
        given(participantRepository.save(any(MatchParticipant.class)))
                .willReturn(new MatchParticipant());

        // when
        assertDoesNotThrow(() -> matchService.joinMatch(1L, 2L));

        // then
        verify(participantRepository).save(any(MatchParticipant.class));
    }

    @Test
    @DisplayName("경기 참여 - 경기 없음 예외")
    void joinMatchMatchNotFound() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matchService.joinMatch(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경기를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("경기 참여 - 사용자 없음 예외")
    void joinMatchUserNotFound() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(userRepository.findById(2L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matchService.joinMatch(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("경기 참여 - 참여 불가능한 상태 예외")
    void joinMatchMatchStatusNotOpen() {
        // given
        testMatch.setMatchStatus(MatchStatus.FULL);
        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(userRepository.findById(2L)).willReturn(Optional.of(regularUser));

        // when & then
        assertThatThrownBy(() -> matchService.joinMatch(1L, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("참여할 수 없는 경기입니다. 현재 상태: FULL");
    }

    @Test
    @DisplayName("경기 참여 - 중복 참가 예외")
    void joinMatchAlreadyJoined() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(userRepository.findById(2L)).willReturn(Optional.of(regularUser));
        given(participantRepository.findByMatchIdAndUserId(1L, 2L))
                .willReturn(Optional.of(new MatchParticipant()));

        // when & then
        assertThatThrownBy(() -> matchService.joinMatch(1L, 2L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 참가 신청한 경기입니다.");
    }

    @Test
    @DisplayName("경기 참여 취소 - 성공")
    void leaveMatchSuccess() {
        // given
        MatchParticipant participant = new MatchParticipant();
        participant.setId(1L);
        participant.setMatch(testMatch);
        participant.setUser(regularUser);

        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(participantRepository.findByMatchIdAndUserId(1L, 2L))
                .willReturn(Optional.of(participant));

        // when
        assertDoesNotThrow(() -> matchService.leaveMatch(1L, 2L));

        // then
        verify(participantRepository).deleteById(1L);
    }

    @Test
    @DisplayName("경기 참여 취소 - 경기 없음 예외")
    void leaveMatchMatchNotFound() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matchService.leaveMatch(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("경기를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("경기 참여 취소 - 참가 내역 없음 예외")
    void leaveMatchParticipantNotFound() {
        // given
        given(matchRepository.findById(1L)).willReturn(Optional.of(testMatch));
        given(participantRepository.findByMatchIdAndUserId(1L, 2L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> matchService.leaveMatch(1L, 2L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("참가 신청 내역을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("경기 삭제 - 성공")
    void deleteMatchByIdSuccess() {
        // given
        Long matchId = 1L;
        given(matchRepository.existsById(matchId)).willReturn(true);
        doNothing().when(matchRepository).deleteById(matchId);

        // when
        assertDoesNotThrow(() -> matchService.deleteMatchById(matchId));

        // then
        verify(matchRepository).existsById(matchId);
        verify(matchRepository).deleteById(matchId);
    }

    @Test
    @DisplayName("경기 삭제 - 경기 없음 예외")
    void deleteMatchByIdNotFound() {
        // given
        Long matchId = 1L;
        given(matchRepository.existsById(matchId)).willReturn(false);

        // when & then
        assertThatThrownBy(() -> matchService.deleteMatchById(matchId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 매치를 찾을 수 없습니다.");

        verify(matchRepository).existsById(matchId);
        verify(matchRepository, never()).deleteById(anyLong());
    }
}
