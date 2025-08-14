package com.nextcloudlab.kickytime.match.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.entity.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
    Optional<MatchParticipant> findByMatchIdAndUserId(Long matchId, Long userId);

    @Query(
            """
        SELECT new com.nextcloudlab.kickytime.match.dto.MyMatchesResponse$MatchInfo(
            m.id,
            mp.id,
            m.location,
            m.matchDateTime,
            m.maxPlayers,
            (SELECT COUNT(p2.id) FROM MatchParticipant p2 WHERE p2.match.id = m.id),
            mp.joinedAt,
            m.matchStatus
        )
        FROM MatchParticipant mp
        JOIN mp.match m
        JOIN mp.user u
        WHERE u.cognitoSub = :cognitoSub
        ORDER BY m.matchDateTime DESC
    """)
    List<MyMatchesResponse.MatchInfo> findMatchParticipantByUserId(
            @Param("cognitoSub") String cognitoSub);
}
