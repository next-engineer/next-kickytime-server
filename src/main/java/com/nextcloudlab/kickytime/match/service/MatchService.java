package com.nextcloudlab.kickytime.match.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nextcloudlab.kickytime.match.controller.MatchCreateRequestDto;
import com.nextcloudlab.kickytime.match.controller.MatchResponseDto;
import com.nextcloudlab.kickytime.match.entity.Match;
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
}
