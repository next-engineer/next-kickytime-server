package com.example.yourproject.service;

import com.example.yourproject.domain.Matching;
import com.example.yourproject.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final MatchingRepository matchingRepository;

    /**
     * 주어진 matchId에 해당하는 매칭을 삭제합니다.
     * @param matchId 삭제할 매칭 ID
     * @throws ResourceNotFoundException 매칭을 찾지 못한 경우
     */
    public void deleteMatchById(Long matchId) {
        Matching match = matchingRepository.findById(matchId)
                .orElseThrow(() -> new ResourceNotFoundException("해당 매칭을 찾을 수 없습니다."));
        matchingRepository.delete(match);
    }
}
