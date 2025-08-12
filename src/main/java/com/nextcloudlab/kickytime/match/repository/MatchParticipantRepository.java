package com.nextcloudlab.kickytime.match.repository;

import com.nextcloudlab.kickytime.match.entity.MatchParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

}
