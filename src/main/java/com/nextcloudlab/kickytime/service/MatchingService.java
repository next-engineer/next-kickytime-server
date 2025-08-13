package com.nextcloudlab.kickytime.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nextcloudlab.kickytime.match.repository.MatchRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchRepository matchRepository;

    @Transactional
    public void deleteMatchById(Long matchId) {
        // match가 존재하는지 확인 (Optional 사용 가능)
        if (!matchRepository.existsById(matchId)) {
            throw new IllegalArgumentException("해당 매치를 찾을 수 없습니다.");
        }

        // match 삭제 (cascade 설정 시 연관된 match_participants도 삭제됨)
        matchRepository.deleteById(matchId);
    }
}
