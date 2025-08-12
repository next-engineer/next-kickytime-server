package com.nextcloudlab.kickytime.match.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nextcloudlab.kickytime.match.entity.MatchParticipant;

public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {}
