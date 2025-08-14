package com.nextcloudlab.kickytime.match.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nextcloudlab.kickytime.match.controller.MatchResponseDto;
import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.repository.MatchParticipantRepository;
import com.nextcloudlab.kickytime.match.repository.MatchRepository;
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
    public List<MatchResponseDto> getAllMatches() {
        List<Match> matches = matchRepository.findAllByOrderByMatchDateTimeDesc();
        return matches.stream().map(MatchResponseDto::new).collect(Collectors.toList());
    }

    // 매칭 삭제 기능
    public void deleteMatchById(Long matchId) {
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException("해당 매치를 찾을 수 없습니다.");
        }

        matchRepository.deleteById(matchId);
    }
}
