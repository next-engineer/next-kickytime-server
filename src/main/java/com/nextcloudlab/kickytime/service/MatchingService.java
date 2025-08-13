package com.nextcloudlab.kickytime.service;

import com.nextcloudlab.kickytime.match.entity.Match;
import com.nextcloudlab.kickytime.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchRepository matchRepository;

    public void deleteMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new IllegalArgumentException("해당 매치를 찾을 수 없습니다."));
        matchRepository.delete(match);
    }
}
