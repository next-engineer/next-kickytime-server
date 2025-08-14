package com.nextcloudlab.kickytime.match.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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

import jakarta.transaction.Transactional;

@Service
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final MatchParticipantRepository participantRepository;
    private final UserRepository userRepository;

    public MatchService(
            MatchRepository matchRepository,
            MatchParticipantRepository participantRepository,
            UserRepository userRepository) {
        this.matchRepository = matchRepository;
        this.participantRepository = participantRepository;
        this.userRepository = userRepository;
    }

    // 전체 경기 목록 조회
    @Transactional()
    public List<MatchResponseDto> getAllMatches() {
        List<Match> matches = matchRepository.findAllByOrderByMatchDateTimeDesc();

        return matches.stream().map(MatchResponseDto::new).collect(Collectors.toList());
    }

    // 경기 개설
    public void createMatch(MatchCreateRequestDto requestDto) {
        User user =
                userRepository
                        .findById(requestDto.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (user.getRole() != RoleEnum.ADMIN) {
            throw new IllegalStateException("관리자 권한이 있는 사용자만 경기를 개설할 수 있습니다.");
        }

        Match match = new Match();
        match.setMatchStatus(MatchStatus.OPEN);
        match.setMatchDateTime(requestDto.getMatchDateTime());
        match.setLocation(requestDto.getLocation());
        match.setMaxPlayers(requestDto.getMaxPlayers());
        match.setCreatedBy(user);

        matchRepository.save(match);
    }

    // 경기 참여 신청
    public void joinMatch(Long matchId, Long userId) {
        Match match =
                matchRepository
                        .findById(matchId)
                        .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));
        User user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 참여 가능한 상태인지 확인
        if (match.getMatchStatus() != MatchStatus.OPEN) {
            throw new IllegalStateException("참여할 수 없는 경기입니다. 현재 상태: " + match.getMatchStatus());
        }

        // 중복 참가 신청 여부 검증
        Optional<MatchParticipant> existingParticipant =
                participantRepository.findByMatchIdAndUserId(matchId, userId);

        if (existingParticipant.isPresent()) {
            throw new IllegalStateException("이미 참가 신청한 경기입니다.");
        }

        // Match status OPEN이고 기참여 기록 없으면
        // 참가자 등록
        MatchParticipant participant = new MatchParticipant();
        participant.setMatch(match);
        participant.setUser(user);
        participantRepository.save(participant);

        // 현재 등록된 참가자 수가 최대 수와 같으면 마감으로 변경
        if (Objects.equals(match.getCurrentParticipantCount(), match.getMaxPlayers())) {
            match.setMatchStatus(MatchStatus.FULL);
        }
    }

    // 경기 참여 취소(일반회원)
    public void leaveMatch(Long matchId, Long userId) {
        Match match =
                matchRepository
                        .findById(matchId)
                        .orElseThrow(() -> new IllegalArgumentException("경기를 찾을 수 없습니다."));

        MatchParticipant participant =
                participantRepository
                        .findByMatchIdAndUserId(matchId, userId)
                        .orElseThrow(() -> new IllegalArgumentException("참가 신청 내역을 찾을 수 없습니다."));

        participantRepository.deleteById(participant.getId());

        // 정원이 다시 비었으면 상태를 OPEN으로 변경
        if (match.isFull() && match.getCurrentParticipantCount() < match.getMaxPlayers()) {
            match.setMatchStatus(MatchStatus.OPEN);
        }
    }

    // 매칭 삭제 기능
    public void deleteMatchById(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException("해당 매치를 찾을 수 없습니다.");
        }

        matchRepository.deleteById(matchId);
    }
}
