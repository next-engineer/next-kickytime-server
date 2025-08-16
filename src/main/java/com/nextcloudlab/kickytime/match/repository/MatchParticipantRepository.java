package com.nextcloudlab.kickytime.match.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nextcloudlab.kickytime.match.dto.MyMatchesResponse;
import com.nextcloudlab.kickytime.match.entity.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {
    @Query(
            """
        SELECT m.id              AS matchId,
                   mp.id             AS participantId,
                   m.location        AS location,
                   m.matchDateTime   AS matchDateTime,
                   m.maxPlayers      AS maxPlayers,
                   (SELECT COUNT(p2.id)
                      FROM MatchParticipant p2
                     WHERE p2.match.id = m.id)           AS currentPlayers,
                   mp.joinedAt       AS joinedAt,
                   m.matchStatus     AS matchStatus
        FROM MatchParticipant mp
        JOIN mp.match m
        JOIN mp.user u
        WHERE u.cognitoSub = :cognitoSub
        ORDER BY m.matchDateTime DESC
    """)
    List<MyMatchesResponse.MatchInfo> findMatchParticipantByUserId(
            @Param("cognitoSub") String cognitoSub);

    @Query(
            """
        SELECT mp
        FROM MatchParticipant mp
        JOIN mp.match m
        JOIN mp.user u
        WHERE u.cognitoSub = :cognitoSub
        AND m.id = :matchId
    """)
    Optional<MatchParticipant> findByMatchIdAndUserId(Long matchId, String cognitoSub);
}
